package com.over2craft.searchchestshop2.Manager;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignsManager {

    public static HashMap<String, HashMap<String, SignWrapper>> signs = new HashMap<>();

    public static void init() {

        if (!Storage.getConfig().contains("storage.storage")) {
            return;
        }

        signs = new HashMap<>();

        for(String path : Storage.getConfig().getConfigurationSection("storage.storage").getKeys(false)){
            HashMap<String, SignWrapper> signs = new HashMap<>();
            for(String path2 : Storage.getConfig().getConfigurationSection("storage.storage."+path).getKeys(false)){

                try {
                    SignWrapper sw = Storage.getConfig().getObject(String.format("storage.storage.%s.%s", path, path2), SignWrapper.class);
                    signs.put(path2, sw);
                } catch (IllegalArgumentException e) {
                    signs.remove(path2);
                    if (e.getMessage().equals("unknown world")) {
                        Bukkit.getLogger().info(String.format("SearchChestShop - Registered sign %s has been removed because the world in which it was does not exists at this moment. If you recently deleted a world, this is the normal behavior", path2));
                    } else {
                        throw e;
                    }
                }
            }

            SignsManager.signs.put(path, signs);
        }
    }

    public static void add(SignWrapper sign) {

        if (signs.containsKey(formatItemId(sign))) {
            signs.get(formatItemId(sign)).put(formatCoordinatesId(sign), sign);
            return;
        }

        HashMap<String, SignWrapper> SignMap = new HashMap<>();
        SignMap.put(formatCoordinatesId(sign), sign);
        signs.put(formatItemId(sign), SignMap);
    }

    public static void remove(SignWrapper sign) {
        if (signs.containsKey(formatItemId(sign))) {
            signs.get(formatItemId(sign)).remove(formatCoordinatesId(sign));

            if (signs.get(formatItemId(sign)).isEmpty()) {
                signs.remove(formatItemId(sign));
            }
        }
    }

    public static HashMap<String, SignWrapper> getSignsFor(String item_id) {
        return signs.get(item_id);
    }

    public static SignWrapper getSign(String item_id, String coordinates) {
        if (!signs.containsKey(item_id)) {
            return null;
        }

        return getSignsFor(item_id).get(coordinates);
    }

    public static List<String> getAllItemIds() {

        if (signs.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(SignsManager.signs.keySet());
    }

    public static List<String> getAllBuyableItemIds() {

        List<String> list = new ArrayList<>();

        signs.forEach((item_id, items) -> {
            items.forEach((coordinates, sign) -> {
                if (sign.getLine(2).contains("B")) {
                    list.add(item_id);
                }
            });
        });

        return list;
    }

    public static List<String> getAllSellableItemIds() {

        List<String> list = new ArrayList<>();

        signs.forEach((item_id, items) -> {
            items.forEach((coordinates, sign) -> {
                if (sign.getLine(2).contains("S")) {
                    list.add(item_id);
                }
            });
        });

        return list;
    }

    private static String formatItemId(SignWrapper sign) {
        return sign.getItemId();
    }

    private static String formatCoordinatesId(SignWrapper sign) {
        return String.format("x:%s;y:%s;z:%s",
                sign.getLocation().getBlockX(),
                sign.getLocation().getBlockY(),
                sign.getLocation().getBlockZ()
        );
    }
}
