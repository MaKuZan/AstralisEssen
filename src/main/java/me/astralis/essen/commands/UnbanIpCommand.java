package me.astralis.essen.commands;

import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

public class UnbanIpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /unbanip <ip|игрок> [причина]");
            return true;
        }

        String targetOrIp = args[0];
        String reason = (args.length > 1)
                ? String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length))
                : "Не указана";

        Bukkit.getBanList(BanList.Type.IP).pardon(targetOrIp);

        MessageUtil.broadcast("messages.unbanip-broadcast",
                "{player}", targetOrIp,
                "{moderator}", sender.getName(),
                "{reason}", reason);

        LogManager.log("unbans.logs",
                sender.getName() + " | used /unbanip " + targetOrIp + " " + reason);
        return true;
    }
}
