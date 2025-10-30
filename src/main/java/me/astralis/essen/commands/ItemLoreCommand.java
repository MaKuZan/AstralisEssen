package me.astralis.essen.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда только для игроков!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cИспользование: /itemlore <add|line> <текст>");
            player.sendMessage("§7Пример: /itemlore 1 Новый текст");
            player.sendMessage("§7Пример: /itemlore add Добавить строку");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) {
            player.sendMessage("§cВозьми предмет в руку!");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (meta.lore() != null) lore = new ArrayList<>(meta.lore());

        String arg1 = args[0];
        String text = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        Component newLine = parseMiniMessage(text);

        if (arg1.equalsIgnoreCase("add")) {
            lore.add(newLine);
        } else {
            try {
                int line = Integer.parseInt(arg1) - 1;
                if (line < 0 || line >= lore.size()) {
                    while (lore.size() <= line) lore.add(Component.text(""));
                }
                lore.set(line, newLine);
            } catch (NumberFormatException e) {
                player.sendMessage("§cНеверный номер строки!");
                return true;
            }
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        player.sendMessage("§aЛор предмета обновлён!");
        return true;
    }

    private Component parseMiniMessage(String input) {
        return MiniMessage.miniMessage().deserialize(
                LegacyComponentSerializer.legacyAmpersand().serialize(
                        LegacyComponentSerializer.legacySection().deserialize(input)
                )
        );
    }
}
