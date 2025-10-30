package me.astralis.essen.punish;

import me.astralis.essen.utils.LogManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MuteManager {
    private static final Map<String, Long> mutes = new ConcurrentHashMap<>();

    private MuteManager() {}

    public static void mute(String moderator, String targetName, long durationMillis, String reason, String prettyTime) {
        long until = (durationMillis < 0) ? -1L : (System.currentTimeMillis() + durationMillis);
        mutes.put(targetName.toLowerCase(), until);

        LogManager.log("mutes.logs",
                moderator + " | used /mute " + targetName + " " +
                        (until == -1 ? "permanent" : prettyTime) + " " + reason);
    }

    public static boolean unmute(String moderator, String targetName, String reason) {
        Long prev = mutes.remove(targetName.toLowerCase());
        if (prev == null) return false;

        LogManager.log("unmutes.logs",
                moderator + " | used /unmute " + targetName + " " +
                        (reason == null || reason.isBlank() ? "NoReason" : reason));
        return true;
    }

    public static boolean isMuted(String name) {
        Long end = mutes.get(name.toLowerCase());
        if (end == null) return false;
        if (end == -1) return true;
        if (System.currentTimeMillis() > end) {
            mutes.remove(name.toLowerCase());
            return false;
        }
        return true;
    }
}
