package de.dermitdehoar.coinsystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class transferCoinsCMD implements CommandExecutor {

    public transferCoinsCMD(Coinsystem plugin) {
        plugin.getCommand("transferCoins").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if (args.length != 4){
            p.sendMessage("§4 You entered the wrong amount of Parameters try: §4 fromAccname coins toPlayer toAccname");
            return false;
        }
        String fromAcc = args[0];
        int coins = parseCoins(args[1],p);
        Player to = Coinsystem.getInstance().getServer().getPlayer(args[2]);
        String toAcc = args[3];
        if(fromAcc != null && toAcc != null && to != null) {
            BankAccountAPI.transferCoins(p, to, coins, fromAcc, toAcc);
            return true;
        }
        else{
            p.sendMessage("You entered the wrong Parameters try: §4fromAccname coins toPlayer toAccname");
        }
        return false;
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