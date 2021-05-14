package com.over2craft.searchchestshop2.Manager;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.Breeze.Utils.PriceUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignWrapper implements ConfigurationSerializable {

    private final String[] lines;

    private final Location location;

    private ItemStack itemStack = null;

    public SignWrapper(String[] lines, Location location) {
        this.lines = lines;
        this.location = location;
    }

    public SignWrapper(String[] lines, Location location, ItemStack itemStack) {
        this.lines = lines;
        this.location = location;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        if (itemStack == null) {
            itemStack = MaterialUtil.getItem(getLine(3));
        }
        return itemStack;
    }

    public BigDecimal getExactBuyPrice() {
        return PriceUtil.getExactBuyPrice(getLine(2));
    }

    public BigDecimal getExactSellPrice() {
        return PriceUtil.getExactSellPrice(getLine(2));
    }

    public boolean hasSellPrice() {
        return !getExactSellPrice().equals(BigDecimal.valueOf(-1));
    }

    public boolean hasBuyPrice() {
        return !getExactBuyPrice().equals(BigDecimal.valueOf(-1));
    }

    public SignWrapper(ArrayList<String> lines, Location location, ItemStack itemStack) {
        this.lines = new String[]{lines.get(0), lines.get(1), lines.get(2), lines.get(3)};
        this.location = location;
        this.itemStack = itemStack;
    }

    public void teleportPlayer(Player player) {

        if (!(getLocation().getBlock().getState() instanceof Sign)
            || !((Sign) getLocation().getBlock().getState()).getLine(3).equals(getLine(3))
            || !((Sign) getLocation().getBlock().getState()).getLine(2).equals(getLine(2))
            || !((Sign) getLocation().getBlock().getState()).getLine(1).equals(getLine(1))
            || !((Sign) getLocation().getBlock().getState()).getLine(0).equals(getLine(0))
        ) {
            SignsManager.remove(this);
            player.sendMessage("§6Over§b2§2Craft §f- Oups... ce shop n'existe plus, il a été supprimé de la liste.");
        }

        player.teleport(getLocation());
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int line) {
        return lines[line];
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("lines", lines);
        serialized.put("location", location);
        serialized.put("item", itemStack);
        return serialized;
    }

    public static SignWrapper deserialize(Map<String, Object> deserialize) {

        return new SignWrapper(
                (ArrayList<String>) deserialize.get("lines"), (Location) deserialize.get("location"), (ItemStack) deserialize.get("item")
        );
    }

    public String getItemId() {
        return getItemStack().getType().toString();
    }
}
