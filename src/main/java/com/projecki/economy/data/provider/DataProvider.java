package com.projecki.economy.data.provider;

import com.projecki.economy.data.PlayerEconomyData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class DataProvider {
    private final Map<UUID, PlayerEconomyData> playerEconomyData = new HashMap<>();

    /**
     * Load the economy data for the given player from the database.
     *
     * @param player the player who's data to load
     * @return a future with the players data
     */
    public abstract CompletableFuture<PlayerEconomyData> loadPlayerData(Player player);

    /**
     * Save the cached economy data for the given player to the database.
     *
     * @param player the player who's data to save
     * @return a future which completes when the data is saved
     */
    public abstract CompletableFuture<Void> savePlayerData(Player player);

    /**
     * Load the economy data for the given player from the database
     * and add it to the cache.
     *
     * For simply fetching the data, use {@link #loadPlayerData}.
     *
     * @param player the player who's data to load and cache
     * @return a future with the players data
     */
    public CompletableFuture<PlayerEconomyData> loadAndCachePlayerData(Player player) {
        return loadPlayerData(player).whenComplete((data, t) -> playerEconomyData.put(player.getUniqueId(), data));
    }

    /**
     * Save the cached economy data for the given player to the database
     * and remove it from the cache.
     *
     * For simply saving the data, use {@link #savePlayerData}.
     *
     * @param player the player who's data to save and cache
     * @return a future which completes when the data is saved
     */
    public CompletableFuture<Void> saveAndUncachePlayerData(Player player) {
        return savePlayerData(player).whenComplete((x, t) -> playerEconomyData.remove(player.getUniqueId()));
    }

    /**
     * Returns the cached economy data for the given player.
     *
     * @param player the player who's data to return
     * @return the players cached economy data, otherwise null
     */
    public PlayerEconomyData getCachedPlayerData(Player player) {
        return playerEconomyData.get(player.getUniqueId());
    }
}
