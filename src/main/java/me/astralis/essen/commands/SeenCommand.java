package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("astralis.seen")) {
            MessageUtil.send(sender, "command-messages.no-permission");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cИспользование: /seen <игрок>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            MessageUtil.send(sender, "seen-unknown", "{player}", args[0]);
            return true;
        }

        if (target.isOnline()) {
            MessageUtil.send(sender, "seen-online", "{player}", target.getName());
        } else {
            Date last = new Date(target.getLastSeen());
            String formatted = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(last);
            MessageUtil.send(sender, "seen-offline",
                    "{player}", target.getName(),
                    "{time}", formatted);
        }
        return true;
    }
}
