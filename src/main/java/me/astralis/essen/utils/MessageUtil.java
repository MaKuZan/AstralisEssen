package me.astralis.essen.utils;

import me.astralis.essen.AstralisEssen;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_AMP = LegacyComponentSerializer.legacyAmpersand();

    // '&#RRGGBB' (твой привычный синтаксис)
    private static final Pattern HEX_AMP_PATTERN = Pattern.compile("(?i)&#([0-9a-f]{6})");
    // '&'-коды форматирования
    private static final Pattern AMP_CODE = Pattern.compile("(?i)&([0-9A-FK-OR])");

    /* ===================== ПУБЛИЧНЫЕ АПИ ===================== */

    /** Сообщение из messages.<path> */
    public static void send(CommandSender sender, String path, String... placeholders) {
        Object raw = AstralisEssen.getInstance().getConfig().get("messages." + normalize(path));
        String prefix = AstralisEssen.getInstance().getConfig().getString("chat.prefix", "");
        if (raw == null) {
            sender.sendMessage("§c[Ошибка] Не найдено сообщение: " + path);
            return;
        }
        if (raw instanceof List<?>) {
            for (String line : (List<String>) raw) {
                sender.sendMessage(parseToComponent(prefix + apply(line, placeholders)));
            }
        } else {
            sender.sendMessage(parseToComponent(prefix + apply(String.valueOf(raw), placeholders)));
        }
    }

    /** Сообщение из command-messages.<path> */
    public static void sendCommandMessage(CommandSender sender, String path, String... placeholders) {
        Object raw = AstralisEssen.getInstance().getConfig().get("command-messages." + normalize(path));
        if (raw == null) {
            sender.sendMessage("§c[Ошибка] Не найдено сообщение: " + path);
            return;
        }
        if (raw instanceof List<?>) {
            for (String line : (List<String>) raw) {
                sender.sendMessage(parseToComponent(apply(line, placeholders)));
            }
        } else {
            sender.sendMessage(parseToComponent(apply(String.valueOf(raw), placeholders)));
        }
    }

    /** Broadcast из messages.<path> */
    public static void broadcast(String path, String... placeholders) {
        Object raw = AstralisEssen.getInstance().getConfig().get("messages." + normalize(path));
        String prefix = AstralisEssen.getInstance().getConfig().getString("chat.prefix", "");
        if (raw == null) return;

        if (raw instanceof List<?>) {
            for (String line : (List<String>) raw) {
                broadcastLine(prefix + apply(line, placeholders));
            }
        } else {
            broadcastLine(prefix + apply(String.valueOf(raw), placeholders));
        }
    }

    /** Простой broadcast произвольной строки */
    public static void broadcastColor(String text) {
        broadcastLine(text);
    }

    /** ГЛАВНЫЙ парсер: поддерживает одновременно MiniMessage + & + &#RRGGBB */
    public static Component parseToComponent(String text) {
        if (text == null || text.isEmpty()) return Component.empty();

        // 1) Нормализуем и подготовим строку
        String s = text.replace('§', '&');        // не смешиваем § с &
        s = convertHashHex(s);                    // '&#RRGGBB' -> '<#RRGGBB>'

        // 2) Если есть MiniMessage-теги ИЛИ hex-цветы — превратим &-коды в теги и отдадим MiniMessage
        if (containsMiniMarkers(s)) {
            s = ampersandToMini(s);               // &a -> <green>, &l -> <bold>, &r -> <reset>, и т.п.
            try {
                return MM.deserialize(s);
            } catch (Exception ignored) {
                // fallback на legacy, если вдруг в строке ошибка MM
                return LEGACY_AMP.deserialize(text.replace('§', '&'));
            }
        }

        // 3) Иначе — это чистая legacy-строка с '&' кодами (быстро и надёжно)
        return LEGACY_AMP.deserialize(s);
    }

    /** Быстрая раскраска старым способом (String, когда нужен именно String) */
    public static String color(String text) {
        if (text == null) return "";
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', text.replace('§', '&'));
    }

    /* ===================== ВНУТРЕННИЕ ХЕЛПЕРЫ ===================== */

    private static String apply(String text, String... placeholders) {
        String t = text;
        for (int i = 0; i + 1 < placeholders.length; i += 2) {
            t = t.replace(placeholders[i], placeholders[i + 1]);
        }
        return t;
    }

    private static String normalize(String path) {
        return path.startsWith("messages.") ? path.substring("messages.".length()) : path;
    }

    private static void broadcastLine(String text) {
        Component comp = parseToComponent(text);
        for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(comp);
        Bukkit.getConsoleSender().sendMessage(LEGACY_AMP.serialize(comp));
    }

    private static boolean containsMiniMarkers(String s) {
        // есть теги вида <...> или hex-тег <#RRGGBB>
        return (s.indexOf('<') != -1 && s.indexOf('>') != -1) || s.contains("<#");
    }

    /** '&#RRGGBB' -> '<#RRGGBB>' */
    private static String convertHashHex(String input) {
        if (input == null || input.isEmpty()) return input;
        Matcher m = HEX_AMP_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (m.find()) m.appendReplacement(sb, "<#" + m.group(1) + ">");
        m.appendTail(sb);
        return sb.toString();
    }

    /** Конвертируем &-коды в MiniMessage-теги, чтобы можно было смешивать с <gradient> и др. */
    private static String ampersandToMini(String s) {
        // быстрый проход по &-кодам
        Matcher m = AMP_CODE.matcher(s);
        StringBuffer out = new StringBuffer();
        while (m.find()) {
            String code = m.group(1).toLowerCase();
            String repl;
            switch (code) {
                case "0": repl = "<black>"; break;
                case "1": repl = "<dark_blue>"; break;
                case "2": repl = "<dark_green>"; break;
                case "3": repl = "<dark_aqua>"; break;
                case "4": repl = "<dark_red>"; break;
                case "5": repl = "<dark_purple>"; break;
                case "6": repl = "<gold>"; break;
                case "7": repl = "<gray>"; break;
                case "8": repl = "<dark_gray>"; break;
                case "9": repl = "<blue>"; break;
                case "a": repl = "<green>"; break;
                case "b": repl = "<aqua>"; break;
                case "c": repl = "<red>"; break;
                case "d": repl = "<light_purple>"; break;
                case "e": repl = "<yellow>"; break;
                case "f": repl = "<white>"; break;
                case "k": repl = "<obfuscated>"; break;
                case "l": repl = "<bold>"; break;
                case "m": repl = "<strikethrough>"; break;
                case "n": repl = "<underlined>"; break;
                case "o": repl = "<italic>"; break;
                case "r": repl = "<reset>"; break;
                default:  repl = ""; break;
            }
            m.appendReplacement(out, Matcher.quoteReplacement(repl));
        }
        m.appendTail(out);
        return out.toString();
    }
}
