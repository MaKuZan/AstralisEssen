package me.astralis.essen.listeners;

import me.astralis.essen.AstralisEssen;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var cfg = AstralisEssen.getInstance().getConfig();

        // Проверка — если выключено, то ничего не выводим
        if (!cfg.getBoolean("join-quit.enable", true)) {
            e.joinMessage(null);
            return;
        }

        // Получаем текст и заменяем %player%
        String msg = cfg.getString("join-quit.join", "&a%player% присоединился к серверу!");
        msg = msg.replace("%player%", e.getPlayer().getName());

        // Поддержка & + MiniMessage + RGB
        e.joinMessage(MessageUtil.parseToComponent(msg));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var cfg = AstralisEssen.getInstance().getConfig();

        // Проверка — если выключено, то ничего не выводим
        if (!cfg.getBoolean("join-quit.enable", true)) {
            e.quitMessage(null);
            return;
        }

        // Получаем текст и заменяем %player%
        String msg = cfg.getString("join-quit.quit", "&c%player% покинул сервер.");
        msg = msg.replace("%player%", e.getPlayer().getName());

        // Поддержка & + MiniMessage + RGB
        e.quitMessage(MessageUtil.parseToComponent(msg));
    }
}
