package me.astralis.essen.commands;

import me.astralis.essen.punish.MuteManager;
import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /mute <игрок> [время] [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = "Нарушение правил";
        long muteEnd = 0;

        if (args.length >= 2 && isTime(args[1])) {
            muteEnd = parseTime(args[1]);
            if (args.length > 2)
                reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        } else if (args.length >= 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        MuteManager.mute(targetName, muteEnd);

        Player target = org.bukkit.Bukkit.getPlayerExact(targetName);
        if (target != null) {
            MessageUtil.send(target, "messages.mute-player",
                    "{reason}", reason,
                    "{time}", (muteEnd > 0 ? args[1] : "permanent"));
        }

        MessageUtil.broadcast("messages.mute-broadcast",
                "{player}", targetName,
                "{moderator}", sender.getName(),
                "{time}", (muteEnd > 0 ? args[1] : "permanent"),
                "{reason}", reason);

        LogManager.log("mutes.logs",
                sender.getName() + " | used /mute " + targetName + " " +
                        (muteEnd > 0 ? args[1] : "permanent") + " " + reason);

        return true;
    }

    private boolean isTime(String s) {
        return s.matches("\\d+[smhdw]");
    }

    private long parseTime(String input) {
        long now = System.currentTimeMillis();
        int value = Integer.parseInt(input.replaceAll("[^0-9]", ""));
        long add = 0;
        if (input.endsWith("s")) add = TimeUnit.SECONDS.toMillis(value);
        else if (input.endsWith("m")) add = TimeUnit.MINUTES.toMillis(value);
        else if (input.endsWith("h")) add = TimeUnit.HOURS.toMillis(value);
        else if (input.endsWith("d")) add = TimeUnit.DAYS.toMillis(value);
        else if (input.endsWith("w")) add = TimeUnit.DAYS.toMillis(value * 7);
        return now + add;
    }
}
