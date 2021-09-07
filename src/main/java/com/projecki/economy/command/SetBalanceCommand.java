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
public class SetBalanceCommand implements CommandExecutor {
    private final SimpleEconomyPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setbalance <player> <new balance>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return true;
        }

        double newBalance;
        try {
            newBalance = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "You have not entered a valid new balance");
            return true;
        }

        PlayerEconomyData data = plugin.getDataProvider().getCachedPlayerData(target);
        data.setBalance(newBalance);

        String monetaryUnit = plugin.getConfig().getString("monetary-unit");
        sender.sendMessage(ChatColor.GREEN + "Updated " + target.getName() + "'s balance to " + monetaryUnit + data.getBalance());
        return true;
    }
}
