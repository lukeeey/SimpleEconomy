package com.projecki.economy.listener;

import com.projecki.economy.data.provider.DataProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerJoinLeaveListener implements Listener {
    private final DataProvider provider;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        provider.loadAndCachePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        provider.saveAndUncachePlayerData(event.getPlayer());
    }
}
