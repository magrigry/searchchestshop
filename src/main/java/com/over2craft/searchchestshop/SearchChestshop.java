package com.over2craft.searchchestshop;

import com.over2craft.searchchestshop.Commands.SearchCommand;
import com.over2craft.searchchestshop.Commands.TeleportCommand;
import com.over2craft.searchchestshop.Manager.SignWrapper;
import com.over2craft.searchchestshop.Manager.SignsManager;
import com.over2craft.searchchestshop.Manager.Storage;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SearchChestshop extends JavaPlugin {

    public static SearchChestshop pl;

    @Override
    public void onEnable() {
        pl = this;
        ConfigurationSerialization.registerClass(SignWrapper.class);
        saveDefaultConfig();
        Storage.saveDefaultConfig();
        SignsManager.init();
        Objects.requireNonNull(getCommand("shopsearch")).setTabCompleter(new SearchCommand());
        Objects.requireNonNull(getCommand("shopsearch")).setExecutor(new SearchCommand());
        Objects.requireNonNull(getCommand("shopteleport")).setExecutor(new TeleportCommand());
        getServer().getPluginManager().registerEvents(new ChestShopListener(), this);

    }

    @Override
    public void onDisable() {
        Storage.getConfig().set("storage.storage", SignsManager.signs);
        Storage.saveConfig();
    }
}
