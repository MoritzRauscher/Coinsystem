package de.dermitdehoar.coinsystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class removeBankCMD implements CommandExecutor {
    public removeBankCMD(Coinsystem plugin){
        plugin.getCommand("removeBankAccount").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(args.length != 1) p.sendMessage("Please only enter the name of the bank Account as Parameter.");
        else{
            BankAccountAPI.removeAccount(p.getName(),args[0]);
            return true;
        }
        return false;
    }
}