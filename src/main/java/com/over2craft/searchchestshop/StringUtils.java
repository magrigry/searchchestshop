package com.over2craft.searchchestshop;

public class StringUtils {

    public static String format(String str) {
        str = String.format("%s%s", SearchChestshop.pl.getConfig().getString("message.prefix"), str);
        str = str.replace("&", "§");
        return str;
    }

    public static String getMessage(String str) {
        return format(SearchChestshop.pl.getConfig().getString(str));
    }

}
