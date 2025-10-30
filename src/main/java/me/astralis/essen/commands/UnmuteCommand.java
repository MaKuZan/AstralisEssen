package me.astralis.essen.commands;

import me.astralis.essen.punish.MuteManager;
import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.command.*;

public class UnmuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /unmute <игрок> [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = (args.length > 1)
                ? String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length))
                : "Не указана";

        if (!MuteManager.isMuted(targetName)) {
            sender.sendMessage("§eИгрок не находится в муте!");
            return true;
        }

        MuteManager.unmute(targetName);

        MessageUtil.broadcast("messages.unmute-broadcast",
                "{player}", targetName,
                "{moderator}", sender.getName(),
                "{reason}", reason);

        LogManager.log("unmutes.logs",
                sender.getName() + " | used /unmute " + targetName + " " + reason);

        return true;
    }
}
