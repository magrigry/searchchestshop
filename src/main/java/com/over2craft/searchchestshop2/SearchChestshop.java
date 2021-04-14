package com.over2craft.searchchestshop2;

import com.over2craft.searchchestshop2.Commands.SearchCommand;
import com.over2craft.searchchestshop2.Manager.SignWrapper;
import com.over2craft.searchchestshop2.Manager.SignsManager;
import com.over2craft.searchchestshop2.Manager.Storage;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class SearchChestshop extends JavaPlugin {

    public static SearchChestshop pl;

    @Override
    public void onEnable() {
        pl = this;
        ConfigurationSerialization.registerClass(SignWrapper.class);
        saveDefaultConfig();
        Objects.requireNonNull(getCommand("shopsearch2")).setTabCompleter(new SearchCommand());
        Objects.requireNonNull(getCommand("shopsearch2")).setExecutor(new SearchCommand());
        getServer().getPluginManager().registerEvents(new ChestShopListener(), this);

        new BukkitRunnable() {

            @Override
            public void run() {
                Storage.saveDefaultConfig();
                SignsManager.init();
            }

        }.runTaskLaterAsynchronously(this, 1200);

    }

    @Override
    public void onDisable() {
        Storage.getConfig().set("storage.storage", SignsManager.signs);
        Storage.saveConfig();
    }

}
