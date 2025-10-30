package me.astralis.essen.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cКоманда только для игроков!");
            return true;
        }

        Player p = (Player) sender;
        boolean fly = !p.getAllowFlight();
        p.setAllowFlight(fly);
        p.sendMessage(fly ? "§aРежим полёта включён!" : "§cРежим полёта выключен!");
        return true;
    }
}
