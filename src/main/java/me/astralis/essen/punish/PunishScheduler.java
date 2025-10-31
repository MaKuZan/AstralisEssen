package me.astralis.essen.punish;

import me.astralis.essen.AstralisEssen;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PunishScheduler {

    private static final Map<String, Long> tempBans = new ConcurrentHashMap<>();
    private static final Map<String, Long> tempMutes = new ConcurrentHashMap<>();

    public static void scheduleUnban(String player, long expire) {
        tempBans.put(player, expire);
    }

    public static void scheduleUnmute(String player, long expire) {
        tempMutes.put(player, expire);
    }

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                // проверка банов
                tempBans.entrySet().removeIf(entry -> {
                    if (now >= entry.getValue()) {
                        Bukkit.getBanList(BanList.Type.NAME).pardon(entry.getKey());
                        AstralisEssen.getInstance().getLogger().info("[AUTO-UNBAN] " + entry.getKey());
                        return true;
                    }
                    return false;
                });

                // проверка мутов
                tempMutes.entrySet().removeIf(entry -> {
                    if (now >= entry.getValue()) {
                        MuteManager.unmute(entry.getKey());
                        AstralisEssen.getInstance().getLogger().info("[AUTO-UNMUTE] " + entry.getKey());
                        return true;
                    }
                    return false;
                });
            }
        }.runTaskTimer(AstralisEssen.getInstance(), 20 * 30, 20 * 30); // каждые 30 секунд
    }
}
