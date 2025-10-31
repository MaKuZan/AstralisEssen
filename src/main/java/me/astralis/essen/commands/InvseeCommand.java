package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько игрок!");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage("§cИспользование: /invsee <игрок>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            MessageUtil.send(p, "tp-not-found");
            return true;
        }

        if (!p.hasPermission("astralis.invsee")) {
            MessageUtil.send(p, "invsee-no-perm");
            return true;
        }

        p.openInventory(target.getInventory());
        MessageUtil.send(p, "invsee-open", "{player}", target.getName());
        return true;
    }
}
