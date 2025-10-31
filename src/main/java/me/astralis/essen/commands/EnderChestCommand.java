package me.astralis.essen.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда только для игроков!");
            return true;
        }

        if (args.length == 0) {
            player.openInventory(player.getEnderChest());
            player.sendMessage("§aВы открыли свой сундук Края.");
            return true;
        }

        if (!player.hasPermission("astralis.ec.others")) {
            player.sendMessage("§cУ вас нет прав открыть чужой эндер-сундук!");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§cИгрок не найден!");
            return true;
        }

        player.openInventory(target.getEnderChest());
        player.sendMessage("§aВы открыли эндер-сундук игрока §e" + target.getName());
        return true;
    }
}
