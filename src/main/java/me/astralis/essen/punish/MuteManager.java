package me.astralis.essen.punish;

import me.astralis.essen.AstralisEssen;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager {

    private static final Map<String, Long> mutedPlayers = new ConcurrentHashMap<>();

    /**
     * Замьютить игрока
     * @param player   ник игрока
     * @param endTime  время окончания мута (в миллисекундах, 0 = навсегда)
     */
    public static void mute(String player, long endTime) {
        mutedPlayers.put(player.toLowerCase(), endTime);
        if (endTime > 0) {
            PunishScheduler.scheduleUnmute(player, endTime);
        }
    }

    /**
     * Проверка — находится ли игрок в муте
     */
    public static boolean isMuted(String player) {
        if (!mutedPlayers.containsKey(player.toLowerCase())) return false;

        long expire = mutedPlayers.get(player.toLowerCase());
        if (expire == 0) return true; // перманентный мут

        if (System.currentTimeMillis() > expire) {
            // истёк срок — снять мут
            unmute(player);
            return false;
        }
        return true;
    }

    /**
     * Размьют игрока (без сообщений)
     */
    public static void unmute(String player) {
        mutedPlayers.remove(player.toLowerCase());
        AstralisEssen.getInstance().getLogger().info("[UNMUTE] " + player);
    }
}
