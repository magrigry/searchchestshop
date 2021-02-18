package com.over2craft.searchchestshop.Commands;

import com.over2craft.searchchestshop.Manager.PlayerWrapper;
import com.over2craft.searchchestshop.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 2 && sender instanceof Player) {
            PlayerWrapper pm = new PlayerWrapper(((Player) sender).getPlayer());
            if (pm.teleportToSign(args[0], args[1])) {
                sender.sendMessage(StringUtils.getMessage("message.teleport"));
            } else {

                sender.sendMessage(StringUtils.getMessage("message.noshop"));
            }
        }

        return false;
    }

}
