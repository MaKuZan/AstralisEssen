package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько игрок!");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("§cИспользование: /tp <игрок> [цель]");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            MessageUtil.send(p, "tp-not-found");
            return true;
        }

        if (args.length == 1) {
            p.teleport(target.getLocation());
            MessageUtil.send(p, "tp-success", "{player}", target.getName());
        } else {
            Player to = Bukkit.getPlayerExact(args[1]);
            if (to == null) {
                MessageUtil.send(p, "tp-not-found");
                return true;
            }
            target.teleport(to.getLocation());
            MessageUtil.send(p, "tp-to-player", "{from}", target.getName(), "{to}", to.getName());
        }
        return true;
    }
}
