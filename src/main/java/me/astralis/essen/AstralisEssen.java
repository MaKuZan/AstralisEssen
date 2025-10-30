package me.astralis.essen;

import me.astralis.essen.commands.*;
import me.astralis.essen.listeners.ChatListener;
import org.bukkit.plugin.java.JavaPlugin;
import me.astralis.essen.punish.PunishScheduler;

public final class AstralisEssen extends JavaPlugin {

    private static AstralisEssen instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig(); // создаёт config.yml при первом запуске
        printBanner();
        PunishScheduler.start();

        // Регистрация команд
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("god").setExecutor(new GodCommand());
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("itemname").setExecutor(new ItemNameCommand());
        getCommand("itemlore").setExecutor(new ItemLoreCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("banip").setExecutor(new BanIpCommand());
        getCommand("unbanip").setExecutor(new UnbanIpCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("essen").setExecutor(new ReloadCommand(this));

        // Слушатель чата
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("=== AstralisEssen выключен! ===");
    }

    private void printBanner() {
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage("§b===============================");
        getServer().getConsoleSender().sendMessage("§3   AstralisEssen §f| §bby Never");
        getServer().getConsoleSender().sendMessage("§7   Version: §f1.0.0");
        getServer().getConsoleSender().sendMessage("§7   Loaded successfully!");
        getServer().getConsoleSender().sendMessage("§b===============================");
        getServer().getConsoleSender().sendMessage("");
    }

    public static AstralisEssen getInstance() {
        return instance;
    }
}
