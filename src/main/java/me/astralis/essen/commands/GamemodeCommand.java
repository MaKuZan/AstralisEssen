package me.astralis.essen.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда доступна только игрокам!");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cИспользование: /gm <режим>");
            player.sendMessage("§7Доступно: survival, creative, adventure, spectator");
            return true;
        }

        String input = args[0].toLowerCase();
        GameMode mode = parseGamemode(input);

        if (mode == null) {
            player.sendMessage("§cНеверный режим! Используй: survival, creative, adventure, spectator");
            return true;
        }

        player.setGameMode(mode);
        player.sendMessage("§aВаш игровой режим установлен на §e" + mode.name().toLowerCase());
        return true;
    }

    private GameMode parseGamemode(String arg) {
        switch (arg) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> modes = Arrays.asList("survival", "creative", "adventure", "spectator");
            String entered = args[0].toLowerCase();
            List<String> result = new ArrayList<>();

            for (String gm : modes) {
                if (gm.startsWith(entered)) result.add(gm);
            }
            return result;
        }
        return null;
    }
}
