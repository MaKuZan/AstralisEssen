package me.astralis.essen.utils;

import me.astralis.essen.AstralisEssen;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    public static void send(CommandSender sender, String path, Object... replace) {
        String raw = AstralisEssen.getInstance().getConfig().getString(path, "&c[!] Сообщение не найдено: " + path);
        sender.sendMessage(apply(raw, replace));
    }

    public static void broadcast(String path, Object... replace) {
        String raw = AstralisEssen.getInstance().getConfig().getString(path, "");
        String msg = apply(raw, replace);
        if (msg != null && !msg.isEmpty()) Bukkit.broadcastMessage(msg);
    }

    public static String apply(String text, Object... replace) {
        if (text == null) return "";
        String out = text;
        for (int i = 0; i < replace.length - 1; i += 2) {
            out = out.replace(String.valueOf(replace[i]), String.valueOf(replace[i + 1]));
        }
        return out.replace("&", "§");
    }
}
