package com.over2craft.searchchestshop.Manager;

import com.Acrobot.Breeze.Utils.PriceUtil;
import com.over2craft.searchchestshop.SearchChestshop;
import com.over2craft.searchchestshop.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class PlayerWrapper {

    Player player;

    public PlayerWrapper(Player player) {
        this.player = player;
    }

    public void teleportToSign(SignWrapper sign) {

        // TODO : Fix teleportation to make the player look at the sign
//        if (SearchChestshop.pl.getConfig().getBoolean("tryToChangeCoordinates")) {
//
//            Location oldLocation = sign.getLocation();
//
//            float yaw;
//
//            if (oldLocation.getYaw() >= 0 && oldLocation.getYaw() < 180) {
//                yaw = oldLocation.getYaw() + 180;
//            } else {
//                yaw = oldLocation.getYaw() - 180;
//            }
//
//            Location newLocation = new Location(
//                    oldLocation.getWorld(),
//                    oldLocation.getX(),
//                    oldLocation.getY(),
//                    oldLocation.getZ(),
//                    yaw,
//                    oldLocation.getPitch()
//            );
//
//            Vector direction = sign.getLocation().getDirection();
//            direction.multiply(1);
//
//            player.teleport(newLocation.add(direction));
//            return;
//        }

        player.teleport(sign.getLocation());
    }

    public boolean teleportToSign(String item_id, String coordinates) {

        SignWrapper sign = SignsManager.getSign(item_id, coordinates);
        if (sign != null) {
            teleportToSign(sign);
            return true;
        }

        return false;
    }

    public void sendListSignsMessageFor(String item_id) {
        HashMap<String, SignWrapper> signs = SignsManager.getSignsFor(item_id);

        if (signs == null) {
            player.sendMessage(StringUtils.getMessage("message.noshopWithThisId"));
            return;
        }

        signs.forEach((coordinates, sign) -> Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "tellraw " + Objects.requireNonNull(player.getPlayer()).getName() + " " +
                tpMessagePlaceholders(
                        Objects.requireNonNull(SearchChestshop.pl.getConfig().getString("teleportationMessage")),
                        sign,
                        coordinates
                )
                ));
    }

    public String tpMessagePlaceholders(String str, SignWrapper sign, String coordinatesId) {

        str = str.replace("%shop_name%", sign.getLine(0));
        str = str.replace("%item_id%", sign.getLine(3).replace(" ", "_"));
        str = str.replace("%coordinates%", coordinatesId);
        str = str.replace("%quantity%", sign.getLine(1));

        BigDecimal price;

        price = PriceUtil.getExactBuyPrice(sign.getLine(2));
        if (price.equals(PriceUtil.NO_PRICE)) {
            str = str.replace("%B_price%", "");
        } else {
            str = str.replace("%B_price%", String.format(
                    Objects.requireNonNull(SearchChestshop.pl.getConfig().getString("B_price")),
                    price.toString()));
        }

        price = PriceUtil.getExactSellPrice(sign.getLine(2));
        if (price.equals(PriceUtil.NO_PRICE)) {
            str = str.replace("%S_price%", "");
        } else {
            str = str.replace("%S_price%", String.format(
                    Objects.requireNonNull(SearchChestshop.pl.getConfig().getString("S_price")),
                    price.toString()));
        }

        return str;
    }

}
