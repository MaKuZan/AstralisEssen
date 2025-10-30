package me.astralis.essen.commands;

import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

public class UnbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /unban <игрок> [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = (args.length > 1)
                ? String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length))
                : "Не указана";

        Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);

        MessageUtil.broadcast("messages.unban-broadcast",
                "{player}", targetName,
                "{moderator}", sender.getName(),
                "{reason}", reason);

        LogManager.log("unbans.logs",
                sender.getName() + " | used /unban " + targetName + " " + reason);

        return true;
    }
}
