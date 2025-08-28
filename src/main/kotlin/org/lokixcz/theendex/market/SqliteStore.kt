package org.lokixcz.theendex.market

import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.time.Instant

class SqliteStore(private val plugin: JavaPlugin) {
    private val dbFile = File(plugin.dataFolder, "market.db")
    private fun connect(): Connection {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        return DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")
    }

    fun init() {
        connect().use { conn ->
            conn.createStatement().use { st ->
                st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS items (" +
                        "material TEXT PRIMARY KEY, base REAL, min REAL, max REAL, current REAL, demand REAL, supply REAL" +
                    ")"
                )
                st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS history (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, material TEXT, time TEXT, price REAL" +
                    ")"
                )
                st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_history_mat_time ON history(material, time)")
            }
        }
    }

    fun loadAll(): Map<Material, MarketItem> {
        val map = mutableMapOf<Material, MarketItem>()
        connect().use { conn ->
            conn.createStatement().use { st ->
                val rs = st.executeQuery("SELECT material, base, min, max, current, demand, supply FROM items")
                while (rs.next()) {
                    val mat = Material.matchMaterial(rs.getString("material")) ?: continue
                    val item = MarketItem(
                        material = mat,
                        basePrice = rs.getDouble("base"),
                        minPrice = rs.getDouble("min"),
                        maxPrice = rs.getDouble("max"),
                        currentPrice = rs.getDouble("current"),
                        demand = rs.getDouble("demand"),
                        supply = rs.getDouble("supply"),
                        history = ArrayDeque()
                    )
                    map[mat] = item
                }
                rs.close()
            }
            // history: last N points per item (we'll let MarketManager cap it)
            map.forEach { (mat, item) ->
                conn.prepareStatement("SELECT time, price FROM history WHERE material=? ORDER BY time ASC").use { ps ->
                    ps.setString(1, mat.name)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        val time = runCatching { Instant.parse(rs.getString("time")) }.getOrNull() ?: continue
                        item.history.addLast(PricePoint(time, rs.getDouble("price")))
                    }
                }
            }
        }
        return map
    }

    fun upsertItem(item: MarketItem) {
        connect().use { conn ->
            conn.prepareStatement(
                "INSERT INTO items(material, base, min, max, current, demand, supply) VALUES(?,?,?,?,?,?,?) " +
                    "ON CONFLICT(material) DO UPDATE SET base=excluded.base, min=excluded.min, max=excluded.max, current=excluded.current, demand=excluded.demand, supply=excluded.supply"
            ).use { ps ->
                ps.setString(1, item.material.name)
                ps.setDouble(2, item.basePrice)
                ps.setDouble(3, item.minPrice)
                ps.setDouble(4, item.maxPrice)
                ps.setDouble(5, item.currentPrice)
                ps.setDouble(6, item.demand)
                ps.setDouble(7, item.supply)
                ps.executeUpdate()
            }
        }
    }

    fun appendHistory(material: Material, point: PricePoint) {
        connect().use { conn ->
            conn.prepareStatement("INSERT INTO history(material, time, price) VALUES(?,?,?)").use { ps ->
                ps.setString(1, material.name)
                ps.setString(2, point.time.toString())
                ps.setDouble(3, point.price)
                ps.executeUpdate()
            }
        }
    }

    fun backupDb() {
        // Simple file copy backup; SQLite has locking, but for light usage this is OK
        if (!dbFile.exists()) return
        val backup = File(plugin.dataFolder, "market_backup.db")
        dbFile.copyTo(backup, overwrite = true)
    }
}
