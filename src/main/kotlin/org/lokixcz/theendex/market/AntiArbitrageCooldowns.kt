package org.lokixcz.theendex.market

import org.bukkit.Material
import org.lokixcz.theendex.Endex
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Tracks sell cooldowns after purchases to prevent instant buy-sell arbitrage exploitation.
 * 
 * Implementation: Per-Item Cooldown (Option A)
 * - Tracks last buy time per player per material
 * - Blocks ALL sales of that material during cooldown period
 * - Simple and effective, with acceptable false-positive trade-off
 * 
 * Trade-off: A player with legitimately mined diamonds cannot sell them 
 * immediately after buying diamonds from the market. This is acceptable
 * because it completely blocks the exploit and is simple to implement.
 * 
 * @author The Endex Development Team
 */
object AntiArbitrageCooldowns {
    
    /**
     * Stores last buy timestamp for each player and material.
     * Structure: playerUUID -> (material -> timestamp)
     */
    private val lastBuyTime = ConcurrentHashMap<UUID, ConcurrentHashMap<Material, Long>>()
    
    /**
     * Record a purchase timestamp for cooldown tracking.
     * Call this whenever a player buys items from the market.
     * 
     * @param playerUUID The UUID of the player who bought items
     * @param material The material that was purchased
     */
    fun recordPurchase(playerUUID: UUID, material: Material) {
        val playerMap = lastBuyTime.getOrPut(playerUUID) { ConcurrentHashMap() }
        playerMap[material] = System.currentTimeMillis()
    }
    
    /**
     * Check if a player is currently under sell cooldown for a material.
     * 
     * @param plugin The plugin instance (for config access)
     * @param playerUUID The UUID of the player attempting to sell
     * @param material The material being sold
     * @return true if cooldown blocks the sale, false if sale is allowed
     */
    fun isOnCooldown(plugin: Endex, playerUUID: UUID, material: Material): Boolean {
        // Check if cooldown system is enabled
        val cooldownEnabled = plugin.config.getBoolean("anti-arbitrage.sell-cooldown.enabled", true)
        if (!cooldownEnabled) return false
        
        // Get cooldown duration from config (default 60 seconds)
        val cooldownSeconds = plugin.config.getInt("anti-arbitrage.sell-cooldown.duration-seconds", 60)
        if (cooldownSeconds <= 0) return false
        
        // Check if player has any purchase record for this material
        val playerMap = lastBuyTime[playerUUID] ?: return false
        val lastPurchaseTime = playerMap[material] ?: return false
        
        // Calculate time elapsed since purchase
        val currentTime = System.currentTimeMillis()
        val elapsedMillis = currentTime - lastPurchaseTime
        val cooldownMillis = cooldownSeconds * 1000L
        
        // Return true if still within cooldown period
        return elapsedMillis < cooldownMillis
    }
    
    /**
     * Get the remaining cooldown time in seconds for a player and material.
     * Returns 0 if no cooldown is active.
     * 
     * @param plugin The plugin instance (for config access)
     * @param playerUUID The UUID of the player
     * @param material The material to check
     * @return Remaining cooldown time in seconds (0 if no cooldown)
     */
    fun getRemainingCooldown(plugin: Endex, playerUUID: UUID, material: Material): Int {
        // Check if cooldown system is enabled
        val cooldownEnabled = plugin.config.getBoolean("anti-arbitrage.sell-cooldown.enabled", true)
        if (!cooldownEnabled) return 0
        
        // Get cooldown duration from config
        val cooldownSeconds = plugin.config.getInt("anti-arbitrage.sell-cooldown.duration-seconds", 60)
        if (cooldownSeconds <= 0) return 0
        
        // Check if player has any purchase record for this material
        val playerMap = lastBuyTime[playerUUID] ?: return 0
        val lastPurchaseTime = playerMap[material] ?: return 0
        
        // Calculate remaining time
        val currentTime = System.currentTimeMillis()
        val elapsedMillis = currentTime - lastPurchaseTime
        val cooldownMillis = cooldownSeconds * 1000L
        val remainingMillis = cooldownMillis - elapsedMillis
        
        return if (remainingMillis > 0) {
            ((remainingMillis + 999) / 1000).toInt() // Round up to seconds
        } else {
            0
        }
    }
    
    /**
     * Clear all cooldown data for a player (useful for logout or cleanup).
     * 
     * @param playerUUID The UUID of the player
     */
    fun clearPlayer(playerUUID: UUID) {
        lastBuyTime.remove(playerUUID)
    }
    
    /**
     * Clear cooldown for a specific material for a player.
     * This can be used by admin commands if needed.
     * 
     * @param playerUUID The UUID of the player
     * @param material The material to clear
     */
    fun clearMaterial(playerUUID: UUID, material: Material) {
        lastBuyTime[playerUUID]?.remove(material)
    }
    
    /**
     * Clear all cooldown data (useful for plugin reload).
     */
    fun clearAll() {
        lastBuyTime.clear()
    }
}
