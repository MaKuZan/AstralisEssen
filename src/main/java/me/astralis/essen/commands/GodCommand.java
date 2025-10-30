package me.astralis.essen.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GodCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cКоманда только для игроков!");
            return true;
        }

        Player player = (Player) sender;
        boolean god = !player.isInvulnerable();
        player.setInvulnerable(god);
        player.sendMessage(god ? "§aРежим Бога активирован!" : "§cРежим Бога отключён!");
        return true;
    }
}
