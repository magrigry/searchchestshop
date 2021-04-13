package com.over2craft.searchchestshop2.Items;

import java.util.HashMap;

public class Categories {

    HashMap<String, String[]> categories = new HashMap<>();

    private static Categories instance;

    public static Categories getInstance() {

        if (instance == null) {
            instance = new Categories();
            instance.init();
        }

        return instance;
    }

    private void init() {

    }

}
