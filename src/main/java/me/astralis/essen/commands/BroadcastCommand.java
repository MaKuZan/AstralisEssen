package me.astralis.essen.commands;

import me.astralis.essen.utils.LogManager;
import me.astralis.essen.utils.MessageUtil;
import me.astralis.essen.AstralisEssen;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cИспользование: /broadcast <сообщение>");
            return true;
        }

        String msg = String.join(" ", args);
        Object raw = AstralisEssen.getInstance().getConfig().get("messages.broadcast-format");

        if (raw == null) {
            Component comp = MessageUtil.parseToComponent("&6&l[Объявление] &r" + msg);
            for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(comp);
            Bukkit.getConsoleSender().sendMessage(msg);
        } else if (raw instanceof List<?>) {
            for (String line : (List<String>) raw) {
                String formatted = line
                        .replace("{message}", msg)
                        .replace("{moderator}", sender.getName());
                Component comp = MessageUtil.parseToComponent(formatted);
                for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(comp);
                Bukkit.getConsoleSender().sendMessage(formatted);
            }
        } else if (raw instanceof String) {
            String formatted = ((String) raw)
                    .replace("{message}", msg)
                    .replace("{moderator}", sender.getName());
            Component comp = MessageUtil.parseToComponent(formatted);
            for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage(comp);
            Bukkit.getConsoleSender().sendMessage(formatted);
        }

        LogManager.log("broadcast.logs",
                sender.getName() + " | used /broadcast " + msg);

        return true;
    }
}
