package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpHereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько игрок!");
            return true;
        }
        if (args.length != 1) {
            p.sendMessage("§cИспользование: /tphere <игрок>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            MessageUtil.send(p, "tp-not-found");
            return true;
        }

        target.teleport(p.getLocation());
        MessageUtil.send(p, "tp-here", "{player}", target.getName());
        return true;
    }
}
