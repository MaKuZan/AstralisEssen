package me.astralis.essen.commands;

import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /ban <игрок> [время] [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = "Нарушение правил";
        Date expireDate = null;

        // ✅ если второй аргумент — время, а не причина
        if (args.length >= 2 && isTime(args[1])) {
            expireDate = parseTime(args[1]);
            if (args.length > 2) {
                reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            }
        } else if (args.length >= 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        // добавить бан
        Bukkit.getBanList(BanList.Type.NAME).addBan(targetName, reason, expireDate, sender.getName());

        Player target = Bukkit.getPlayerExact(targetName);
        if (target != null) {
            MessageUtil.send(target, "messages.ban-player",
                    "{reason}", reason,
                    "{time}", (expireDate != null ? args[1] : "permanent"));
            target.kickPlayer("§cВы были забанены!\n§7Причина: §f" + reason);
        }

        // ✅ теперь подставляется правильное время
        MessageUtil.broadcast("messages.ban-broadcast",
                "{player}", targetName,
                "{moderator}", sender.getName(),
                "{time}", (expireDate != null ? args[1] : "permanent"),
                "{reason}", reason);

        LogManager.log("bans.logs",
                sender.getName() + " | used /ban " + targetName + " " +
                        (expireDate != null ? args[1] : "permanent") + " " + reason);

        return true;
    }

    private boolean isTime(String s) {
        return s.matches("\\d+[smhdw]");
    }

    private Date parseTime(String input) {
        long now = System.currentTimeMillis();
        int value = Integer.parseInt(input.replaceAll("[^0-9]", ""));
        long add = 0;
        if (input.endsWith("s")) add = TimeUnit.SECONDS.toMillis(value);
        else if (input.endsWith("m")) add = TimeUnit.MINUTES.toMillis(value);
        else if (input.endsWith("h")) add = TimeUnit.HOURS.toMillis(value);
        else if (input.endsWith("d")) add = TimeUnit.DAYS.toMillis(value);
        else if (input.endsWith("w")) add = TimeUnit.DAYS.toMillis(value * 7);
        return new Date(now + add);
    }
}
