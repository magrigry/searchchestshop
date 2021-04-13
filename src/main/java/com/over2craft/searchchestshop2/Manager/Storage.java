package com.over2craft.searchchestshop2.Manager;

import com.over2craft.searchchestshop2.SearchChestshop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Storage {

    private static File file = new File(SearchChestshop.pl.getDataFolder()+"/storage.yml");
    private static FileConfiguration config;

    public static void reloadConfig() {
        file = new File(SearchChestshop.pl.getDataFolder()+"/storage.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveDefaultConfig() {

        config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            saveConfig();
        }
    }

    public static FileConfiguration getConfig() {

        return config;
    }

    public static void saveConfig() {

        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
