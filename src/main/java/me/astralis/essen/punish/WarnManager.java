package me.astralis.essen.punish;

import me.astralis.essen.AstralisEssen;
import me.astralis.essen.utils.LogManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WarnManager {

    private static final Map<String, Long> warnedPlayers = new ConcurrentHashMap<>();
    private static final Map<String, Integer> warnCounts = new ConcurrentHashMap<>();
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static void warn(String moderator, String player, String reason) {
        AstralisEssen plugin = AstralisEssen.getInstance();
        long duration = parseDuration(plugin.getConfig().getString("warn.duration", "1h"));
        long expire = System.currentTimeMillis() + duration;
        warnedPlayers.put(player.toLowerCase(), expire);

        int current = warnCounts.getOrDefault(player.toLowerCase(), 0) + 1;
        warnCounts.put(player.toLowerCase(), current);

        // Минимесседж сообщение
        String msg = plugin.getConfig().getString("warn.message", "<red>{player} получил варн от {moderator}");
        msg = msg.replace("{player}", player)
                .replace("{moderator}", moderator)
                .replace("{reason}", reason);
        Bukkit.broadcast(mm.deserialize(msg));

        LogManager.log("warns.logs", moderator + " | used /warn " + player + " " + reason);

        // автоудаление варна
        new BukkitRunnable() {
            @Override
            public void run() {
                if (warnedPlayers.remove(player.toLowerCase()) != null) {
                    String rmsg = plugin.getConfig().getString("warn.remove-message", "<green>Варн с {player} истёк.");
                    rmsg = rmsg.replace("{player}", player);
                    Bukkit.broadcast(mm.deserialize(rmsg));

                    LogManager.log("unwarns.logs", "[AUTO] снят варн " + player);

                    // уменьшить счётчик варнов
                    int count = warnCounts.getOrDefault(player.toLowerCase(), 1);
                    warnCounts.put(player.toLowerCase(), Math.max(0, count - 1));
                }
            }
        }.runTaskLater(plugin, duration / 50);

        // Проверяем лимит
        int limit = plugin.getConfig().getInt("warn-limit.count", 3);
        if (current >= limit) {
            executeLimitActions(player, moderator, reason);
            warnCounts.put(player.toLowerCase(), 0); // сброс после выполнения
        }
    }

    private static void executeLimitActions(String player, String moderator, String reason) {
        List<String> actions = AstralisEssen.getInstance().getConfig().getStringList("warn-limit.actions");
        if (actions == null || actions.isEmpty()) return;

        for (String action : actions) {
            String cmd = action
                    .replace("{player}", player)
                    .replace("{moderator}", moderator)
                    .replace("{reason}", reason);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }

        AstralisEssen.getInstance().getLogger().info("[WARN] Выполнены действия за 3 варна для " + player);
    }

    private static long parseDuration(String input) {
        long value = Long.parseLong(input.replaceAll("[^0-9]", ""));
        if (input.endsWith("s")) return value * 1000;
        if (input.endsWith("m")) return value * 60_000;
        if (input.endsWith("h")) return value * 3_600_000;
        if (input.endsWith("d")) return value * 86_400_000;
        if (input.endsWith("w")) return value * 604_800_000;
        return value * 60_000; // по умолчанию — минута
    }

    public static boolean isWarned(String player) {
        Long end = warnedPlayers.get(player.toLowerCase());
        if (end == null) return false;
        if (System.currentTimeMillis() > end) {
            warnedPlayers.remove(player.toLowerCase());
            return false;
        }
        return true;
    }

    public static int getWarnCount(String player) {
        return warnCounts.getOrDefault(player.toLowerCase(), 0);
    }
}
