package me.astralis.essen;

import me.astralis.essen.commands.*;
import me.astralis.essen.listeners.ChatListener;
import me.astralis.essen.listeners.JoinQuitListener;
import me.astralis.essen.listeners.PlayerBanListener;
import me.astralis.essen.punish.PunishScheduler;
import org.bukkit.plugin.java.JavaPlugin;

public final class AstralisEssen extends JavaPlugin {

    private static AstralisEssen instance;

    public static AstralisEssen getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getLogger().info("=== AstralisEssen включён! ===");
        PunishScheduler.start();

        // ⚙️ Регистрация команд
        getCommand("essen").setExecutor(new ReloadCommand(this));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("god").setExecutor(new GodCommand());

        GamemodeCommand gmCmd = new GamemodeCommand();
        getCommand("gm").setExecutor(gmCmd);
        getCommand("gm").setTabCompleter(gmCmd);

        getCommand("itemname").setExecutor(new ItemNameCommand());
        getCommand("itemlore").setExecutor(new ItemLoreCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("ec").setExecutor(new EnderChestCommand());

        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("banip").setExecutor(new BanIpCommand());
        getCommand("unbanip").setExecutor(new UnbanIpCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("warnings").setExecutor(new WarningsCommand());

        getCommand("kick").setExecutor(new KickCommand());
        getCommand("tp").setExecutor(new TpCommand());
        getCommand("tphere").setExecutor(new TpHereCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("smite").setExecutor(new SmiteCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("clearchat").setExecutor(new ClearChatCommand());
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("seen").setExecutor(new SeenCommand());
        getCommand("essreload").setExecutor(new ReloadCommand(this));

        // 🎧 Слушатели
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBanListener(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("=== AstralisEssen выключен! ===");
    }
}
