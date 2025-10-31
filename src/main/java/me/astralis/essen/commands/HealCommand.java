package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("§cТолько игрок может исцелить себя!");
                return true;
            }

            p.setHealth(p.getMaxHealth());
            p.setFireTicks(0);
            p.setFoodLevel(20);
            MessageUtil.send(p, "heal-self");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            MessageUtil.send(sender, "command-messages.player-not-found");
            return true;
        }

        target.setHealth(target.getMaxHealth());
        target.setFireTicks(0);
        target.setFoodLevel(20);

        MessageUtil.send(target, "healed-by", "{moderator}", sender.getName());
        MessageUtil.send(sender, "heal-other", "{player}", target.getName());
        return true;
    }
}
