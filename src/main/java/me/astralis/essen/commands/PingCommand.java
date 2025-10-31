package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько игрок!");
            return true;
        }

        int ping = p.getPing();
        MessageUtil.send(p, "ping-self", "{ping}", String.valueOf(ping));
        return true;
    }
}
