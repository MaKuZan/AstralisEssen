package me.astralis.essen.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemNameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда только для игроков!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cИспользование: /itemname <новое имя>");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) {
            player.sendMessage("§cВозьми предмет в руку!");
            return true;
        }

        String input = String.join(" ", args);
        Component name = parseMiniMessage(input);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);

        player.sendMessage("§aНазвание предмета изменено!");
        return true;
    }

    private Component parseMiniMessage(String input) {
        // поддержка HEX (#RRGGBB) и MiniMessage (<gradient:red:blue>)
        return MiniMessage.miniMessage().deserialize(
                LegacyComponentSerializer.legacyAmpersand().serialize(
                        LegacyComponentSerializer.legacySection().deserialize(input)
                )
        );
    }
}
