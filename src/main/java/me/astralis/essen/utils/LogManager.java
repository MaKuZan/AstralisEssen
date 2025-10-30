package me.astralis.essen.utils;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    private static final String LOG_DIR = "plugins/AstralisEssen/logs/";

    public static void log(String fileName, String message) {
        try {
            File folder = new File(LOG_DIR);
            if (!folder.exists()) folder.mkdirs();

            File logFile = new File(folder, fileName);
            if (!logFile.exists()) logFile.createNewFile();

            FileWriter writer = new FileWriter(logFile, true);
            String time = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
            writer.write("(" + time + ") | " + message + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            Bukkit.getLogger().warning("[AstralisEssen] Ошибка записи лога: " + e.getMessage());
        }
    }
}
