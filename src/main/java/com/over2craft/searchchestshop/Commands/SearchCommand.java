package com.over2craft.searchchestshop.Commands;

import com.over2craft.searchchestshop.Manager.PlayerWrapper;
import com.over2craft.searchchestshop.Manager.SignsManager;
import com.over2craft.searchchestshop.Manager.Storage;
import com.over2craft.searchchestshop.SearchChestshop;
import com.over2craft.searchchestshop.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchCommand implements TabCompleter, CommandExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 0) {
            return SignsManager.getAllItemIds().stream().
                    filter(value -> value.toUpperCase().contains(args[0].toUpperCase()))
                    .limit(SearchChestshop.pl.getConfig().getInt("limitAutoCompleteSuggestion"))
                    .collect(Collectors.toList());
        }

        if (args.length == 1) {
            List<String> suggest = SignsManager.getAllItemIds().stream().
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
            PlayerWrapper pm = new PlayerWrapper(((Player) sender).getPlayer());
            pm.sendListSignsMessageFor(args[0]);
            return true;
        }

        if (args.length == 2 && sender instanceof Player) {
            PlayerWrapper pm = new PlayerWrapper(((Player) sender).getPlayer());
            pm.sendListSignsMessageFor(args[1]);
            return true;
        }

        sender.sendMessage(StringUtils.getMessage("message.shopSearchUsage"));
        return true;
    }
}
