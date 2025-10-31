package me.astralis.essen.commands;

import me.astralis.essen.AstralisEssen;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand implements CommandExecutor {

    private final AstralisEssen plugin;

    public ReloadCommand(AstralisEssen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.reloadConfig();

        // Если сообщение — список строк
        Object raw = plugin.getConfig().get("messages.config-reload");
        if (raw instanceof List<?>) {
            for (String line : (List<String>) raw) {
                sender.sendMessage(MessageUtil.parseToComponent(line));
            }
        } else if (raw instanceof String) {
            sender.sendMessage(MessageUtil.parseToComponent((String) raw));
        } else {
            sender.sendMessage(MessageUtil.parseToComponent("&aКонфиг AstralisEssen успешно перезагружен!"));
        }

        return true;
    }
}
