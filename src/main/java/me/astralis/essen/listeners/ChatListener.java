package me.astralis.essen.listeners;

import me.astralis.essen.punish.MuteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String name = event.getPlayer().getName();
        if (MuteManager.isMuted(name)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cВы не можете писать в чат — вы замьючены!");
        }
    }
}
