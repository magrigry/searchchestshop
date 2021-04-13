package com.over2craft.searchchestshop2.Commands;

import com.over2craft.searchchestshop2.Inventories.SmartInv;
import com.over2craft.searchchestshop2.Items.FrenchTranslation;
import com.over2craft.searchchestshop2.Manager.SignsFilter;
import com.over2craft.searchchestshop2.Manager.SignsManager;
import com.over2craft.searchchestshop2.Manager.Storage;
import com.over2craft.searchchestshop2.SearchChestshop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchCommand implements TabCompleter, CommandExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 0) {

            List<String> suggest = SignsManager.getAllItemIds();

            suggest.addAll(FrenchTranslation.getTranslation().values());

            suggest = suggest.stream().
                    filter(value -> value.toUpperCase().contains(args[0].toUpperCase()))
                    .limit(SearchChestshop.pl.getConfig().getInt("limitAutoCompleteSuggestion"))
                    .collect(Collectors.toList());

            return suggest;
        }

        if (args.length == 1) {

            List<String> suggest = SignsManager.getAllItemIds();

            suggest.addAll(FrenchTranslation.getTranslation().values());

            suggest = suggest.stream().
                    filter(value -> value.toUpperCase().contains(args[0].toUpperCase()))
                    .limit(SearchChestshop.pl.getConfig().getInt("limitAutoCompleteSuggestion"))
                    .collect(Collectors.toList());

            suggest.add(SearchChestshop.pl.getConfig().getString("sellable"));
            suggest.add(SearchChestshop.pl.getConfig().getString("buyable"));

            if (sender.hasPermission("over2craft.shopsearch.reload")) {
                suggest.add("$reload");
                suggest.add("$reload-storage");
            }

            return suggest;
        }

        if (args.length == 2) {

            if (Objects.equals(SearchChestshop.pl.getConfig().getString("sellable"), args[0])) {
                return SignsManager.getAllSellableItemIds().stream()
                        .limit(SearchChestshop.pl.getConfig().getInt("limitAutoCompleteSuggestion"))
                        .collect(Collectors.toList());
            }

            if (Objects.equals(SearchChestshop.pl.getConfig().getString("buyable"), args[0])) {
                return SignsManager.getAllBuyableItemIds().stream()
                        .limit(SearchChestshop.pl.getConfig().getInt("limitAutoCompleteSuggestion"))
                        .collect(Collectors.toList());
            }

            return new ArrayList<>();
        }

        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equals("$reload")) {
            SearchChestshop.pl.reloadConfig();
            sender.sendMessage("SearchChestShop config.yml reloaded.");
            return true;
        }

        if (args.length == 1 && args[0].equals("$reload-storage")) {
            Storage.reloadConfig();
            SignsManager.init();
            sender.sendMessage("SearchChestShop storage.yml reloaded.");
            return true;
        }

        if (args.length == 1 && sender instanceof Player) {
            SmartInv.getInventory(new SignsFilter(SignsManager.signs).LikeItemIdWithFr(args[0])).open((Player) sender);
            return true;
        }

        if (args.length == 2 && sender instanceof Player) {
            SmartInv.getInventory(new SignsFilter(SignsManager.signs).LikeItemIdWithFr(args[1])).open((Player) sender);
            return true;
        }

        if (sender instanceof HumanEntity) {
            SmartInv.getInventory(new SignsFilter(SignsManager.signs)).open((Player) sender);
        }

        return true;
    }
}
