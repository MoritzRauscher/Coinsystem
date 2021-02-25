package de.dermitdehoar.coinsystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsGUICMD implements CommandExecutor {

    public CoinsGUICMD(Coinsystem plugin){
        plugin.getCommand("bank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        Interest interest = Coinsystem.getInterest();
        interest.startInterest();
        BanksystemGUIInventory bankGUI = new BanksystemGUIInventory(p,"§6Banksystem",27);
        bankGUI.setItems();
        bankGUI.open();
        return true;
    }
}
