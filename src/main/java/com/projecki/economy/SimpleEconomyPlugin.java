package com.projecki.economy;

import com.projecki.economy.command.BalanceCommand;
import com.projecki.economy.command.SetBalanceCommand;
import com.projecki.economy.data.provider.DataProvider;
import com.projecki.economy.data.provider.MySQLDataProvider;
import com.projecki.economy.listener.PlayerJoinLeaveListener;
import com.projecki.economy.util.AsyncUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleEconomyPlugin extends JavaPlugin {
    @Getter
    private static SimpleEconomyPlugin instance;

    @Getter
    private DataProvider dataProvider;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        dataProvider = new MySQLDataProvider(this);

        new AsyncUtils();

        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("setbalance").setExecutor(new SetBalanceCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(dataProvider), this);
    }
}