package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cЭта команда только для игроков!");
            return true;
        }

        if (args.length != 2) {
            MessageUtil.send(p, "speed-usage");
            return true;
        }

        String type = args[0].toLowerCase();
        float speed;

        try {
            speed = Float.parseFloat(args[1]) / 10f;
        } catch (NumberFormatException e) {
            MessageUtil.send(p, "speed-invalid");
            return true;
        }

        if (speed < 0 || speed > 1) {
            MessageUtil.send(p, "speed-invalid");
            return true;
        }

        if (type.equals("fly")) {
            p.setFlySpeed(speed);
        } else if (type.equals("walk")) {
            p.setWalkSpeed(speed);
        } else {
            MessageUtil.send(p, "speed-usage");
            return true;
        }

        MessageUtil.send(p, "speed-set",
                "{type}", type,
                "{value}", args[1]);
        return true;
    }
}
