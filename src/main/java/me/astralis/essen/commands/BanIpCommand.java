package me.astralis.essen.commands;

import me.astralis.essen.punish.PunishScheduler;
import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BanIpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /banip <игрок|ip> [время] [причина]");
            return true;
        }

        String targetOrIp = args[0];
        String reason = "Нарушение правил";
        Date expireDate = null;

        if (args.length >= 2 && isTime(args[1])) {
            expireDate = parseTime(args[1]);
            if (args.length > 2) reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        } else if (args.length >= 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        String ipToBan = targetOrIp;
        Player target = Bukkit.getPlayerExact(targetOrIp);
        if (target != null)
            ipToBan = target.getAddress().getAddress().getHostAddress();

        Bukkit.getBanList(BanList.Type.IP).addBan(ipToBan, reason, expireDate, sender.getName());
        if (expireDate != null) PunishScheduler.scheduleUnban(ipToBan, expireDate.getTime());

        MessageUtil.broadcast("messages.banip-broadcast",
                "{player}", ipToBan,
                "{moderator}", sender.getName(),
                "{time}", (expireDate != null ? args[1] : "permanent"),
                "{reason}", reason);

        LogManager.log("bans.logs",
                sender.getName() + " | used /banip " + ipToBan + " " +
                        (expireDate != null ? args[1] : "permanent") + " " + reason);

        if (target != null) target.kickPlayer("§cВы были забанены по IP!\n§7Причина: §f" + reason);
        return true;
    }

    private boolean isTime(String s) {
        return s.matches("\\d+[smhdw]");
    }

    private Date parseTime(String input) {
        long now = System.currentTimeMillis();
        int v = Integer.parseInt(input.replaceAll("[^0-9]", ""));
        long add = 0;
        if (input.endsWith("s")) add = TimeUnit.SECONDS.toMillis(v);
        else if (input.endsWith("m")) add = TimeUnit.MINUTES.toMillis(v);
        else if (input.endsWith("h")) add = TimeUnit.HOURS.toMillis(v);
        else if (input.endsWith("d")) add = TimeUnit.DAYS.toMillis(v);
        else if (input.endsWith("w")) add = TimeUnit.DAYS.toMillis(v * 7);
        return new Date(now + add);
    }
}
