package me.astralis.essen.commands;

import me.astralis.essen.punish.WarnManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользование: /warn <игрок> <причина>");
            return true;
        }

        String targetName = args[0];
        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        WarnManager.warn(sender.getName(), target.getName(), reason);
        sender.sendMessage("§aВы выдали предупреждение игроку §e" + target.getName() + "§a.");
        return true;
    }
}
