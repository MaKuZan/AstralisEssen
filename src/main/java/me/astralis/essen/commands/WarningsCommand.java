package me.astralis.essen.commands;

import me.astralis.essen.punish.WarnManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class WarningsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            int count = WarnManager.getWarnCount(player.getName());
            player.sendMessage("§eУ вас активных варнов: §c" + count);
            return true;
        }

        if (args.length == 1) {
            int count = WarnManager.getWarnCount(args[0]);
            sender.sendMessage("§eУ игрока §a" + args[0] + " §eактивных варнов: §c" + count);
            return true;
        }

        sender.sendMessage("§cИспользование: /warnings [ник]");
        return true;
    }
}
