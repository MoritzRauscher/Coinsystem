package de.dermitdehoar.coinsystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class createBankCMD implements CommandExecutor {
    public createBankCMD(Coinsystem plugin){
        plugin.getCommand("createBankAccount").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(args.length != 1) p.sendMessage("§4 Please only enter the name of the bank Account as Parameter.");
        else{BankAccountAPI.createAccount(p.getName(),0,args[0]);
        return true;
        }
        return false;
    }
}
