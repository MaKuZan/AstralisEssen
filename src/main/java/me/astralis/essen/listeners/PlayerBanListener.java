package me.astralis.essen.listeners;

import me.astralis.essen.AstralisEssen;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import net.kyori.adventure.text.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayerBanListener implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        BanEntry ban = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(event.getName());
        if (ban == null) return;

        String moderator = ban.getSource() != null ? ban.getSource() : "Консоль";
        String reason = ban.getReason() != null ? ban.getReason() : "Нарушение правил";
        Date expiry = ban.getExpiration();
        String dateStr = (expiry != null)
                ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(expiry)
                : "Навсегда";

        // читаем шаблон из конфига
        List<String> lines = AstralisEssen.getInstance().getConfig().getStringList("messages.ban-player");

        StringBuilder msg = new StringBuilder();
        if (lines != null && !lines.isEmpty()) {
            for (String line : lines) {
                line = line.replace("{moderator}", moderator)
                        .replace("{reason}", reason)
                        .replace("{time}", (expiry == null ? "permanent" : ""))
                        .replace("{date}", dateStr)
                        .replace("{player}", event.getName());
                msg.append(line).append("\n");
            }
        } else {
            msg.append("&cВы были забанены! Причина: &f").append(reason);
        }

        Component comp = MessageUtil.parseToComponent(msg.toString().trim());
        event.disallow(Result.KICK_BANNED, comp);
    }
}
