package com.over2craft.searchchestshop;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.over2craft.searchchestshop.Manager.SignWrapper;
import com.over2craft.searchchestshop.Manager.SignsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChestShopListener implements Listener {

    @EventHandler
    public void onShopCreated(ShopCreatedEvent e) {
        if (shopShouldBeReferenced(e.getSignLine((short) 0))) {
            SignsManager.add(new SignWrapper(e.getSignLines(), e.getSign().getLocation()));
        }
    }

    @EventHandler
    public void onTransaction(TransactionEvent e) {
        if (SearchChestshop.pl.getConfig().getBoolean("referenceOnTransactionEvent")) {
            SignsManager.add(new SignWrapper(e.getSign().getLines(), e.getSign().getLocation()));
        }
    }

    @EventHandler
    public void onShopDestroy(ShopDestroyedEvent e) {
        SignsManager.remove(new SignWrapper(e.getSign().getLines(), e.getSign().getLocation()));
    }

    private boolean shopShouldBeReferenced(String shopName) {
        Pattern pattern = Pattern.compile(Objects.requireNonNull(SearchChestshop.pl.getConfig().getString("shopThatshouldBeReferenced")));
        Matcher matcher = pattern.matcher(shopName);
        return matcher.find();
    }

}
