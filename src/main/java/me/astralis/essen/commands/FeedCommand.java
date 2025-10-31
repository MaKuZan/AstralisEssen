package me.astralis.essen.commands;

import me.astralis.essen.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("§cТолько игрок может накормить себя!");
                return true;
            }

            p.setFoodLevel(20);
            p.setSaturation(20);
            MessageUtil.send(p, "feed-self");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            MessageUtil.send(sender, "command-messages.player-not-found");
            return true;
        }

        target.setFoodLevel(20);
        target.setSaturation(20);

        MessageUtil.send(target, "fed-by", "{moderator}", sender.getName());
        MessageUtil.send(sender, "feed-other", "{player}", target.getName());
        return true;
    }
}
