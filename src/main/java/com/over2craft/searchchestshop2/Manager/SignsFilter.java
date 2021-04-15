package com.over2craft.searchchestshop2.Manager;

import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.over2craft.searchchestshop2.Items.FrenchTranslation;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignsFilter {

    public boolean stackFiltered = false;
    public boolean sellOrByFiltered = false;
    public boolean playerOnly = false;

    public LinkedHashMap<String, HashMap<String, SignWrapper>> signs = new LinkedHashMap<>();

    public SignsFilter(HashMap<String, HashMap<String, SignWrapper>> signs) {
        signs.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    this.signs.put(entry.getKey(), entry.getValue());
                });
    }

    public SignsFilter sellableOnly() {

        sellOrByFiltered = true;

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            HashMap<String, SignWrapper> newItems = new HashMap<>();

            items.forEach((coordinates, sign) -> {
                if (!sign.getExactSellPrice().equals(BigDecimal.valueOf(-1))) {
                    newItems.put(coordinates, sign);
                }
            });

            if (!newItems.isEmpty()) {
                newSigns.put(item_id, newItems);
            }
        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter buyableOnly() {

        sellOrByFiltered = true;

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            HashMap<String, SignWrapper> newItems = new HashMap<>();

            items.forEach((coordinates, sign) -> {
                if (!sign.getExactBuyPrice().equals(BigDecimal.valueOf(-1))) {
                    newItems.put(coordinates, sign);
                }
            });

            if (!newItems.isEmpty()) {
                newSigns.put(item_id, newItems);
            }
        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter LikeItemId(String itemIdToCompare) {

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            if (item_id.toLowerCase().contains(itemIdToCompare.toLowerCase())) {
                newSigns.put(item_id, items);
            }

        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter itemId(String itemIdToCompare) {

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            if (item_id.toLowerCase().equals(itemIdToCompare.toLowerCase())) {
                newSigns.put(item_id, items);
            }

        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter LikeItemIdWithFr(String itemIdToCompare) {

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            if (
                    item_id.toLowerCase().contains(itemIdToCompare.toLowerCase())
                            || FrenchTranslation.getTranslation().containsKey(item_id.toLowerCase())
                            && FrenchTranslation.getTranslation().get(item_id.toLowerCase()).toLowerCase().contains(itemIdToCompare.toLowerCase())

            ) {
                newSigns.put(item_id, items);
            }

        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter notStackable() {

        stackFiltered = true;

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            HashMap<String, SignWrapper> newItems = new HashMap<>();

            items.forEach((coordinates, sign) -> {
                if (sign.getItemStack().getType().getMaxStackSize() <= 1) {
                    newItems.put(coordinates, sign);
                }
            });

            if (!newItems.isEmpty()) {
                newSigns.put(item_id, newItems);
            }
        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter stackable() {

        stackFiltered = true;

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            HashMap<String, SignWrapper> newItems = new HashMap<>();

            items.forEach((coordinates, sign) -> {
                if (sign.getItemStack().getType().getMaxStackSize() > 1) {
                    newItems.put(coordinates, sign);
                }
            });

            if (!newItems.isEmpty()) {
                newSigns.put(item_id, newItems);
            }
        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter playerOnly() {

        playerOnly = true;

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            for (Map.Entry<String, SignWrapper> entry : items.entrySet()) {
                SignWrapper sign = entry.getValue();
                if (!ChestShopSign.isAdminShop(sign.getLine(0))) {
                    newSigns.put(item_id, items);
                    break;
                }
            }
        });

        this.signs = newSigns;
        return this;
    }

    public SignsFilter player(String playerName) {

        playerOnly = true;

        LinkedHashMap<String, HashMap<String, SignWrapper>> newSigns = new LinkedHashMap<>();

        signs.forEach((item_id, items) -> {

            for (Map.Entry<String, SignWrapper> entry : items.entrySet()) {
                SignWrapper sign = entry.getValue();
                if (sign.getLine(0).toLowerCase().equals(playerName.toLowerCase())) {

                    newSigns.put(item_id, items);
                    break;
                }
            }
        });

        this.signs = newSigns;
        return this;
    }


}
