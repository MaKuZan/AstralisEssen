package me.astralis.essen.commands;

import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cКоманда только для игроков!");
            return true;
        }

        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage("§cИспользование: /gm <0|1|2|3>");
            return true;
        }

        switch (args[0]) {
            case "0" -> p.setGameMode(GameMode.SURVIVAL);
            case "1" -> p.setGameMode(GameMode.CREATIVE);
            case "2" -> p.setGameMode(GameMode.ADVENTURE);
            case "3" -> p.setGameMode(GameMode.SPECTATOR);
            default -> {
                p.sendMessage("§cНеверный режим! Используй: 0, 1, 2, 3");
                return true;
            }
        }
        p.sendMessage("§aРежим игры установлен на: §e" + p.getGameMode());
        return true;
    }
}
