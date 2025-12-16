package org.lokixcz.theendex.tracking

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.block.Container
import org.bukkit.block.DoubleChest
import org.bukkit.block.ShulkerBox
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.lokixcz.theendex.Endex
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.Callable

/**
 * WorldStorageScanner: Scans all container block entities in loaded chunks to
 * provide global item totals for dynamic pricing.
 *
 * Performance Optimizations:
 * - Scans only loaded chunks (no chunk loading)
 * - Uses async executor for aggregation, sync for Bukkit API calls
 * - Chunked batch processing to avoid blocking main thread
 * - Configurable scan interval with aggressive caching
 * - Early exit if scan already in progress
 * - Container type filtering (skip unwanted container types)
 * - Shulker box nested inventory support (optional)
 * - Double chest deduplication to prevent double-counting
 * - Per-chunk item cap to prevent storage farm manipulation
 * - TPS-aware throttling to reduce lag on busy servers
 */
class WorldStorageScanner(private val plugin: Endex) {

    // Configuration
    private val enabledFlag: Boolean = plugin.config.getBoolean("price-world-storage.enabled", false)
    private val scanIntervalSeconds: Int = plugin.config.getInt("price-world-storage.scan-interval-seconds", 300).coerceAtLeast(60)
    private val includeChests: Boolean = plugin.config.getBoolean("price-world-storage.containers.chests", true)
    private val includeBarrels: Boolean = plugin.config.getBoolean("price-world-storage.containers.barrels", true)
    private val includeShulkers: Boolean = plugin.config.getBoolean("price-world-storage.containers.shulker-boxes", true)
    private val includeHoppers: Boolean = plugin.config.getBoolean("price-world-storage.containers.hoppers", false)
    private val includeDroppers: Boolean = plugin.config.getBoolean("price-world-storage.containers.droppers", false)
    private val includeDispensers: Boolean = plugin.config.getBoolean("price-world-storage.containers.dispensers", false)
    private val includeFurnaces: Boolean = plugin.config.getBoolean("price-world-storage.containers.furnaces", false)
    private val includeBrewingStands: Boolean = plugin.config.getBoolean("price-world-storage.containers.brewing-stands", false)
    private val scanShulkerContents: Boolean = plugin.config.getBoolean("price-world-storage.scan-shulker-contents", true)
    private val chunksPerTick: Int = plugin.config.getInt("price-world-storage.chunks-per-tick", 50).coerceIn(10, 200)
    private val excludedWorlds: Set<String> = plugin.config.getStringList("price-world-storage.excluded-worlds").map { it.lowercase() }.toSet()
    
    // Anti-manipulation settings
    private val perChunkItemCap: Int = plugin.config.getInt("price-world-storage.anti-manipulation.per-chunk-item-cap", 10000).coerceAtLeast(100)
    private val perMaterialChunkCap: Int = plugin.config.getInt("price-world-storage.anti-manipulation.per-material-chunk-cap", 5000).coerceAtLeast(64)
    private val minTpsForScan: Double = plugin.config.getDouble("price-world-storage.anti-manipulation.min-tps", 18.0).coerceIn(10.0, 20.0)
    private val logSuspiciousActivity: Boolean = plugin.config.getBoolean("price-world-storage.anti-manipulation.log-suspicious", true)

    // Cache
    @Volatile
    private var cachedTotals: Map<Material, Long> = emptyMap()

    @Volatile
    private var lastScanEpoch: Long = 0L

    @Volatile
    private var lastScanDurationMs: Long = 0L

    @Volatile
    private var lastContainerCount: Int = 0
    
    @Volatile
    private var skippedDueToTps: Boolean = false

    private val scanning = AtomicBoolean(false)
    private val scanCounter = AtomicLong(0L)

    // Scheduler
    private var executor: ScheduledExecutorService? = null
    private var scheduledTask: ScheduledFuture<*>? = null
    
    // Track scanned double chests to avoid double-counting (location hash set per scan)
    // Using Location.hashCode isn't reliable, so we use "world:x:y:z" strings
    private val scannedDoubleChests = ConcurrentHashMap.newKeySet<String>()

    // Container type names for filtering
    private val allowedContainerTypes: Set<String> by lazy {
        buildSet {
            if (includeChests) {
                add("CHEST")
                add("TRAPPED_CHEST")
                // Note: ENDER_CHEST excluded - per-player storage
            }
            if (includeBarrels) add("BARREL")
            if (includeShulkers) {
                // All shulker box colors
                Material.entries.filter { it.name.endsWith("SHULKER_BOX") }.forEach { add(it.name) }
            }
            if (includeHoppers) add("HOPPER")
            if (includeDroppers) add("DROPPER")
            if (includeDispensers) add("DISPENSER")
            if (includeFurnaces) {
                add("FURNACE")
                add("BLAST_FURNACE")
                add("SMOKER")
            }
            if (includeBrewingStands) add("BREWING_STAND")
        }
    }

    fun enabled(): Boolean = enabledFlag

    fun start() {
        if (!enabledFlag) {
            plugin.logger.info("WorldStorageScanner disabled in config.")
            return
        }
        executor = Executors.newSingleThreadScheduledExecutor { r ->
            Thread(r, "Endex-WorldStorageScanner").apply { isDaemon = true }
        }
        // Initial scan after 10 seconds, then repeat at configured interval
        scheduledTask = executor?.scheduleAtFixedRate(
            { triggerScan() },
            10L,
            scanIntervalSeconds.toLong(),
            TimeUnit.SECONDS
        )
        plugin.logger.info("WorldStorageScanner started (interval=${scanIntervalSeconds}s, chunksPerTick=$chunksPerTick)")
    }

    fun stop() {
        scheduledTask?.cancel(false)
        executor?.shutdown()
        try {
            executor?.awaitTermination(5, TimeUnit.SECONDS)
        } catch (_: InterruptedException) {
            executor?.shutdownNow()
        }
        executor = null
        scheduledTask = null
        plugin.logger.info("WorldStorageScanner stopped.")
    }

    /**
     * Get cached totals. Returns empty map if scanner is disabled or no scan completed yet.
     * This is the primary method called by MarketManager during price updates.
     */
    fun snapshotTotals(): Map<Material, Long> = cachedTotals

    /**
     * Get the epoch second of the last completed scan.
     */
    fun lastScanTime(): Long = lastScanEpoch

    /**
     * Get duration of the last scan in milliseconds.
     */
    fun lastScanDuration(): Long = lastScanDurationMs

    /**
     * Get number of containers scanned in last scan.
     */
    fun lastContainerCount(): Int = lastContainerCount

    /**
     * Get total number of scans completed since plugin start.
     */
    fun scanCount(): Long = scanCounter.get()
    
    /**
     * Check if last scan was skipped due to low TPS.
     */
    fun wasSkippedDueToTps(): Boolean = skippedDueToTps

    /**
     * Manually trigger a scan (e.g., from admin command).
     * Returns false if a scan is already in progress.
     */
    fun triggerScan(): Boolean {
        if (!enabledFlag) return false
        if (!scanning.compareAndSet(false, true)) {
            // Scan already in progress
            return false
        }
        performScanAsync()
        return true
    }

    /**
     * Force refresh - blocks until scan completes (for admin commands).
     * Should only be called from async context or with caution.
     */
    fun forceRefresh(): Map<Material, Long> {
        if (!enabledFlag) return emptyMap()
        // Wait for any in-progress scan
        var waited = 0
        while (scanning.get() && waited < 300) { // Max 30 seconds wait
            Thread.sleep(100)
            waited++
        }
        if (scanning.get()) return cachedTotals // Give up waiting
        // Trigger new scan and wait
        triggerScan()
        waited = 0
        while (scanning.get() && waited < 300) {
            Thread.sleep(100)
            waited++
        }
        return cachedTotals
    }
    
    /**
     * Get current server TPS (approximate).
     */
    private fun getCurrentTps(): Double {
        return try {
            // Paper/Spigot TPS method
            Bukkit.getTPS()[0].coerceIn(0.0, 20.0)
        } catch (_: Exception) {
            20.0 // Assume good TPS if we can't check
        }
    }

    private fun performScanAsync() {
        val startTime = System.currentTimeMillis()
        
        // Clear double-chest tracking for this scan
        scannedDoubleChests.clear()

        // Check TPS before scanning (sync call)
        val tpsFuture = Bukkit.getScheduler().callSyncMethod(plugin, Callable {
            getCurrentTps()
        })

        // Run aggregation in executor thread
        executor?.execute {
            try {
                // TPS check
                val currentTps = try {
                    tpsFuture.get(5, TimeUnit.SECONDS)
                } catch (_: Exception) {
                    20.0
                }
                
                if (currentTps < minTpsForScan) {
                    if (logSuspiciousActivity) {
                        plugin.logger.info("WorldStorageScanner: Skipping scan due to low TPS (${String.format("%.1f", currentTps)} < $minTpsForScan)")
                    }
                    skippedDueToTps = true
                    scanning.set(false)
                    return@execute
                }
                skippedDueToTps = false
                
                // Gather chunks from main thread (Bukkit API requirement)
                val chunksFuture = Bukkit.getScheduler().callSyncMethod(plugin, Callable {
                    gatherChunksToScan()
                })
                
                val chunks = try {
                    chunksFuture.get(30, TimeUnit.SECONDS)
                } catch (e: Exception) {
                    plugin.logger.warning("WorldStorageScanner: Failed to gather chunks: ${e.message}")
                    scanning.set(false)
                    return@execute
                }

                if (chunks.isEmpty()) {
                    cachedTotals = emptyMap()
                    lastScanEpoch = System.currentTimeMillis() / 1000L
                    lastScanDurationMs = System.currentTimeMillis() - startTime
                    lastContainerCount = 0
                    scanCounter.incrementAndGet()
                    scanning.set(false)
                    return@execute
                }

                // Process chunks in batches on main thread
                val totals = ConcurrentHashMap<Material, Long>()
                var containerCount = 0
                val batchSize = chunksPerTick
                val batches = chunks.chunked(batchSize)
                
                // Dynamic delay based on TPS
                val baseDelay = 50L

                for ((batchIndex, batch) in batches.withIndex()) {
                    // Re-check TPS periodically during long scans
                    if (batchIndex > 0 && batchIndex % 10 == 0) {
                        val midScanTps = try {
                            val f = Bukkit.getScheduler().callSyncMethod(plugin, Callable { getCurrentTps() })
                            f.get(2, TimeUnit.SECONDS)
                        } catch (_: Exception) { 20.0 }
                        
                        if (midScanTps < minTpsForScan - 2.0) {
                            if (logSuspiciousActivity) {
                                plugin.logger.warning("WorldStorageScanner: Aborting scan mid-way due to TPS drop (${String.format("%.1f", midScanTps)})")
                            }
                            // Save partial results
                            break
                        }
                    }
                    
                    val batchResult = try {
                        val future = Bukkit.getScheduler().callSyncMethod(plugin, Callable {
                            processBatch(batch)
                        })
                        future.get(10, TimeUnit.SECONDS)
                    } catch (e: Exception) {
                        plugin.logger.warning("WorldStorageScanner: Batch $batchIndex failed: ${e.message}")
                        BatchResult(emptyMap(), 0, emptyList())
                    }

                    // Merge batch results (respecting per-material caps already applied)
                    for ((mat, qty) in batchResult.totals) {
                        totals.merge(mat, qty) { a, b -> a + b }
                    }
                    containerCount += batchResult.containerCount
                    
                    // Log suspicious chunks
                    if (logSuspiciousActivity && batchResult.suspiciousChunks.isNotEmpty()) {
                        for (warning in batchResult.suspiciousChunks) {
                            plugin.logger.warning("WorldStorageScanner: $warning")
                        }
                    }

                    // Adaptive delay between batches
                    if (batchIndex < batches.size - 1) {
                        Thread.sleep(baseDelay)
                    }
                }

                // Update cache atomically
                cachedTotals = totals.toMap()
                lastScanEpoch = System.currentTimeMillis() / 1000L
                lastScanDurationMs = System.currentTimeMillis() - startTime
                lastContainerCount = containerCount
                scanCounter.incrementAndGet()

                if (plugin.config.getBoolean("logging.verbose", false)) {
                    plugin.logger.info("WorldStorageScanner: Scanned $containerCount containers in ${chunks.size} chunks (${lastScanDurationMs}ms)")
                }

            } catch (e: Exception) {
                plugin.logger.warning("WorldStorageScanner: Scan failed: ${e.message}")
            } finally {
                scanning.set(false)
                scannedDoubleChests.clear() // Cleanup
            }
        }
    }

    private fun gatherChunksToScan(): List<ChunkRef> {
        val result = mutableListOf<ChunkRef>()
        for (world in Bukkit.getWorlds()) {
            if (world.name.lowercase() in excludedWorlds) continue
            for (chunk in world.loadedChunks) {
                result.add(ChunkRef(world.name, chunk.x, chunk.z))
            }
        }
        return result
    }

    private data class ChunkRef(val worldName: String, val x: Int, val z: Int)
    private data class BatchResult(val totals: Map<Material, Long>, val containerCount: Int, val suspiciousChunks: List<String>)

    private fun processBatch(chunkRefs: List<ChunkRef>): BatchResult {
        val totals = HashMap<Material, Long>()
        var containerCount = 0
        val suspiciousChunks = mutableListOf<String>()

        for (ref in chunkRefs) {
            val world = Bukkit.getWorld(ref.worldName) ?: continue
            val chunk: Chunk = try {
                if (!world.isChunkLoaded(ref.x, ref.z)) continue
                world.getChunkAt(ref.x, ref.z)
            } catch (_: Exception) {
                continue
            }

            // Per-chunk totals for anti-manipulation cap
            val chunkTotals = HashMap<Material, Long>()
            var chunkTotalItems = 0L

            // Get tile entities (block states) from chunk
            val tileEntities: Array<BlockState> = try {
                chunk.tileEntities
            } catch (_: Exception) {
                continue
            }

            for (state in tileEntities) {
                if (state !is Container) continue
                val typeName = state.block.type.name
                if (typeName !in allowedContainerTypes) continue

                // Skip ender chests (they're per-player, handled by InventorySnapshotService)
                if (typeName == "ENDER_CHEST") continue
                
                val inventory: Inventory = try {
                    state.inventory
                } catch (_: Exception) {
                    continue
                }
                
                // Double chest deduplication: only count once per double chest
                val holder = inventory.holder
                if (holder is DoubleChest) {
                    val loc = holder.location
                    if (loc != null) {
                        val key = "${loc.world?.name}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
                        if (!scannedDoubleChests.add(key)) {
                            // Already scanned this double chest from its other half
                            continue
                        }
                    }
                }

                containerCount++
                scanInventory(inventory, chunkTotals)
            }
            
            // Calculate total items in this chunk
            chunkTotalItems = chunkTotals.values.sum()
            
            // Check for suspicious activity (potential storage farm)
            if (chunkTotalItems > perChunkItemCap) {
                suspiciousChunks.add("Chunk [${ref.worldName}:${ref.x},${ref.z}] has $chunkTotalItems items (cap: $perChunkItemCap) - capping contribution")
            }
            
            // Apply per-chunk caps and merge into global totals
            for ((mat, qty) in chunkTotals) {
                // Cap per-material contribution from this chunk
                val cappedQty = qty.coerceAtMost(perMaterialChunkCap.toLong())
                
                // Only add if total chunk items aren't over cap, or proportionally reduce
                val multiplier = if (chunkTotalItems > perChunkItemCap) {
                    perChunkItemCap.toDouble() / chunkTotalItems.toDouble()
                } else 1.0
                
                val finalQty = (cappedQty * multiplier).toLong().coerceAtLeast(0)
                if (finalQty > 0) {
                    totals.merge(mat, finalQty) { a, b -> a + b }
                }
            }
        }

        return BatchResult(totals, containerCount, suspiciousChunks)
    }

    private fun scanInventory(inventory: Inventory, totals: MutableMap<Material, Long>) {
        for (stack in inventory.contents) {
            if (stack == null || stack.type.isAir) continue
            val amount = stack.amount.toLong()
            if (amount <= 0) continue

            totals.merge(stack.type, amount) { a, b -> a + b }

            // If this is a shulker box item (in inventory), scan its contents too
            if (scanShulkerContents && stack.type.name.endsWith("SHULKER_BOX")) {
                scanShulkerBoxItem(stack, totals)
            }
        }
    }

    private fun scanShulkerBoxItem(stack: ItemStack, totals: MutableMap<Material, Long>) {
        val meta = stack.itemMeta as? org.bukkit.inventory.meta.BlockStateMeta ?: return
        val blockState = try {
            meta.blockState
        } catch (_: Exception) {
            return
        }
        if (blockState !is ShulkerBox) return

        val shulkerInventory = try {
            blockState.inventory
        } catch (_: Exception) {
            return
        }

        for (innerStack in shulkerInventory.contents) {
            if (innerStack == null || innerStack.type.isAir) continue
            val amount = innerStack.amount.toLong()
            if (amount <= 0) continue
            totals.merge(innerStack.type, amount) { a, b -> a + b }
            // Note: We don't recurse further (no shulkers in shulkers in vanilla)
        }
    }
}
