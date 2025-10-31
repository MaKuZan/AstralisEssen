package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (int i = 0; i < 150; i++) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.sendMessage(MessageUtil.color("&r"));
            }
        }

        MessageUtil.broadcast("clearchat-success",
                "{moderator}", sender.getName());
        return true;
    }
}
