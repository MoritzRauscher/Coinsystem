package de.dermitdehoar.coinsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsAddCoinsCMD implements CommandExecutor {

    public CoinsAddCoinsCMD(Coinsystem plugin){
        plugin.getCommand("addcoins").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(args.length == 0) p.sendMessage("§4 Please enter the value you want to add.");
        int coins = parseCoins(args[0],p);
        CoinsAPI.addCoins(p.getName(),coins);
        coins = CoinsAPI.getCoins(p.getName());
        p.sendMessage("§2 You now have " + coins + " Coins in your Inventory.");
        return true;
    }
    int parseCoins(String text,Player player){
        try {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException e){
            player.sendMessage("§4 You didnt entered a valid Integer");
            return 0;
        }
    }
}
