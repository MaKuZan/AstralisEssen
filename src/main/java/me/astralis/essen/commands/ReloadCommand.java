package me.astralis.essen.commands;

import me.astralis.essen.AstralisEssen;
import me.astralis.essen.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final AstralisEssen plugin;

    public ReloadCommand(AstralisEssen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.reloadConfig();
        MessageUtil.send(sender, "messages.config-reload");
        return true;
    }
}
