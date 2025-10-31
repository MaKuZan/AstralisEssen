package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /kick <игрок> [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = (args.length > 1)
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "Без причины";

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        MessageUtil.send(target, "kick-message",
                "{moderator}", sender.getName(),
                "{reason}", reason);
        target.kickPlayer("§c" + reason);

        MessageUtil.broadcast("kick-broadcast",
                "{player}", target.getName(),
                "{moderator}", sender.getName(),
                "{reason}", reason);
        return true;
    }
}
