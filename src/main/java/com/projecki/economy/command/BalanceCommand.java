package com.projecki.economy.command;

import com.projecki.economy.SimpleEconomyPlugin;
import com.projecki.economy.data.PlayerEconomyData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BalanceCommand implements CommandExecutor {
    private final SimpleEconomyPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /balance <player>");
                return true;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found");
                return true;
            }
        }
        PlayerEconomyData data = plugin.getDataProvider().getCachedPlayerData(target);
        String monetaryUnit = plugin.getConfig().getString("monetary-unit", "$");

        sender.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + monetaryUnit + data.getBalance());
        return true;
    }
}
