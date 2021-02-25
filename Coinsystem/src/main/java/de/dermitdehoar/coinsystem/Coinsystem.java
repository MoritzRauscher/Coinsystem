package de.dermitdehoar.coinsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class Coinsystem extends JavaPlugin{

    public static String    WELCOME = "WELCOME TO MY COINSYSTEM";
    private static Coinsystem instance;
    private static Interest interest;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        interest = new Interest();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + WELCOME);
        MySQL.connect();
        new CoinsGUICMD(this);
        new CoinsAddCoinsCMD(this);
        new createBankCMD(this);
        new removeBankCMD(this);
        new transferCoinsCMD(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQL.disconnected();
    }
    public static Coinsystem getInstance(){
        return instance;
    }
    public static Interest getInterest() {return interest;}
}
