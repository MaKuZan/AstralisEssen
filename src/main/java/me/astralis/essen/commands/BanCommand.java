package me.astralis.essen.commands;

import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import me.astralis.essen.AstralisEssen;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendCommandMessage(sender, "invalid-args", "%usage%", "/ban <игрок> [время|permanent] [причина]");
            return true;
        }

        String targetName = args[0];
        String reason = "Нарушение правил";
        Date expireDate = null;
        String timeToken = "permanent";

        // определяем срок
        if (args.length >= 2 && isTime(args[1])) {
            timeToken = args[1];
            expireDate = parseTime(args[1]);
            if (args.length > 2)
                reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        } else if (args.length >= 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        // добавляем бан
        Bukkit.getBanList(BanList.Type.NAME).addBan(targetName, reason, expireDate, sender.getName());

        Player target = Bukkit.getPlayerExact(targetName);
        if (target != null) {
            // формируем меню кика
            List<String> lines = AstralisEssen.getInstance().getConfig().getStringList("messages.ban-player");
            String dateStr = (expireDate != null)
                    ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(expireDate)
                    : "Навсегда";

            if (lines != null && !lines.isEmpty()) {
                StringBuilder fullMessage = new StringBuilder();
                for (String line : lines) {
                    line = line.replace("{moderator}", sender.getName())
                            .replace("{reason}", reason)
                            .replace("{time}", timeToken)
                            .replace("{date}", dateStr)
                            .replace("{player}", target.getName());
                    fullMessage.append(line).append("\n");
                }

                // теперь отправляем на экран отключения, а не в чат
                Component kickComponent = MessageUtil.parseToComponent(fullMessage.toString().trim());
                target.kick(kickComponent);
            } else {
                target.kick(MessageUtil.parseToComponent("&cВы были забанены! Причина: &f" + reason));
            }
        }

        // глобальное сообщение
        MessageUtil.broadcast("ban-broadcast",
                "{player}", targetName,
                "{moderator}", sender.getName(),
                "{time}", timeToken,
                "{reason}", reason);

        // лог
        LogManager.log("bans.logs",
                sender.getName() + " | used /ban " + targetName + " " + timeToken + " " + reason);

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
