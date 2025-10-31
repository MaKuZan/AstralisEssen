package me.astralis.essen.commands;

import me.astralis.essen.punish.MuteManager;
import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import me.astralis.essen.AstralisEssen;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendCommandMessage(sender, "invalid-args", "%usage%", "/mute <игрок> [время|permanent] [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = "Нарушение правил";
        long muteEnd = 0;
        Date expireDate = null;

        // определяем срок мута
        if (args.length >= 2 && isTime(args[1])) {
            muteEnd = parseTime(args[1]);
            expireDate = new Date(muteEnd);
            if (args.length > 2)
                reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        } else if (args.length >= 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        MuteManager.mute(targetName, muteEnd);

        Player target = Bukkit.getPlayerExact(targetName);
        if (target != null) {
            // готовим формат даты
            String dateStr = (expireDate != null)
                    ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(expireDate)
                    : "Навсегда";
            String timeToken = (muteEnd > 0 ? args[1] : "permanent");

            // получаем список строк из config.yml
            List<String> lines = AstralisEssen.getInstance().getConfig().getStringList("messages.mute-player");

            if (lines != null && !lines.isEmpty()) {
                for (String line : lines) {
                    line = line.replace("{moderator}", sender.getName())
                            .replace("{reason}", reason)
                            .replace("{time}", timeToken)
                            .replace("{date}", dateStr)
                            .replace("{player}", target.getName());
                    target.sendMessage(MessageUtil.parseToComponent(line));
                }
            } else {
                // fallback если блок не найден
                target.sendMessage(MessageUtil.parseToComponent("&eВы были замьючены! Причина: &f" + reason));
            }
        }

        // отправляем сообщение в чат (broadcast)
        MessageUtil.broadcast("mute-broadcast",
                "{player}", targetName,
                "{moderator}", sender.getName(),
                "{time}", (muteEnd > 0 ? args[1] : "permanent"),
                "{reason}", reason);

        // логируем
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
