package com.projecki.economy.data.provider;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.projecki.economy.SimpleEconomyPlugin;
import com.projecki.economy.data.PlayerEconomyData;
import com.projecki.economy.util.AsyncUtils;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MySQLDataProvider extends DataProvider {
    private final SimpleEconomyPlugin plugin;
    private Connection connection;

    public MySQLDataProvider(SimpleEconomyPlugin plugin) {
        this.plugin = plugin;

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(plugin.getConfig().getString("database.mysql.host"));
        dataSource.setPort(plugin.getConfig().getInt("database.mysql.port"));
        dataSource.setUser(plugin.getConfig().getString("database.mysql.user"));
        dataSource.setPassword(plugin.getConfig().getString("database.mysql.password"));
        dataSource.setDatabaseName(plugin.getConfig().getString("database.mysql.database"));

        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to the MySQL database");
            e.printStackTrace();
        }

        setupDatabase();
    }

    private void setupDatabase() {
        try {
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `player_data` (" +
                    "`id` VARCHAR(40) NOT NULL," +
                    "`balance` DOUBLE DEFAULT 0 NOT NULL," +
                    "`creation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                    "PRIMARY KEY (`id`));");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<PlayerEconomyData> loadPlayerData(Player player) {
        double startingBalance = plugin.getConfig().getDouble("starting-balance");
        return AsyncUtils.supplyAsync(() -> {
            try {
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `player_data` WHERE `id` = ?;");
                stmt.setString(1, player.getUniqueId().toString());

                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    return new PlayerEconomyData(UUID.fromString(result.getString(1)), result.getTimestamp(3).toInstant(), result.getDouble(2));
                }
                return new PlayerEconomyData(player.getUniqueId(), Instant.now(), startingBalance);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> savePlayerData(Player player) {
        PlayerEconomyData cachedData = getCachedPlayerData(player);
        return AsyncUtils.supplyAsync(() -> {
            try {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO `player_data` (`id`, `balance`, `creation_date`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `balance` = ?;");
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setDouble(2, cachedData.getBalance());
                stmt.setTimestamp(3, Timestamp.from(cachedData.getCreationDate()));
                stmt.setDouble(4, cachedData.getBalance());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
