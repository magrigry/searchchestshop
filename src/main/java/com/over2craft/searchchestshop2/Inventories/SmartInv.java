package com.over2craft.searchchestshop2.Inventories;

import com.over2craft.searchchestshop2.Manager.SignsFilter;
import fr.minuskube.inv.SmartInventory;

public class SmartInv {
    public static SmartInventory getInventory(SignsFilter signsFilter) {
        return SmartInventory.builder()
                .provider(new ItemsInventory(signsFilter))
                .size(6, 9)
                .title("Hôtel des ventes")
                .build();
    }


}
