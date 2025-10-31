package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SmiteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cИспользование: /smite <игрок>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        Location loc = target.getLocation();
        loc.getWorld().strikeLightningEffect(loc);

        MessageUtil.send(sender, "smite-struck", "{player}", target.getName());
        MessageUtil.send(target, "smite-hit", "{moderator}", sender.getName());
        return true;
    }
}
