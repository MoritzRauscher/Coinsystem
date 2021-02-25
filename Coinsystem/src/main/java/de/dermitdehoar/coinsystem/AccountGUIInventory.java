package de.dermitdehoar.coinsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AccountGUIInventory implements Listener {

    private Player p;
    private String name;
    private Inventory inv;
    private ItemStack depositCoins;
    private  ItemStack removeCoins;
    private ItemStack exit;
    private ItemStack showCoins;
    private  ItemStack showAcc;
    private ItemStack back;

    public AccountGUIInventory(Player p, String name, int slots){
        this.p = p;
        this.name = name;
        inv = Bukkit.createInventory(p,slots,name);
        Coinsystem.getInstance().getServer().getPluginManager().registerEvents(this,Coinsystem.getInstance());
    }
    public void setItems() {
        depositCoins = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta itemMeta = depositCoins.getItemMeta();
        itemMeta.setDisplayName(Color("§2deposit all Coins"));
        depositCoins.setItemMeta(itemMeta);
        inv.setItem(10,depositCoins);

        removeCoins = new ItemStack(Material.GOLD_NUGGET);
        itemMeta = removeCoins.getItemMeta();
        itemMeta.setDisplayName(Color("§6remove all Coins"));
        removeCoins.setItemMeta(itemMeta);
        inv.setItem(14,removeCoins);

        exit = new ItemStack(Material.RED_CONCRETE);
        itemMeta = exit.getItemMeta();
        itemMeta.setDisplayName(Color("§4EXIT"));
        exit.setItemMeta(itemMeta);
        inv.setItem(26,exit);

        back = new ItemStack(Material.ORANGE_CONCRETE);
        itemMeta = back.getItemMeta();
        itemMeta.setDisplayName(Color("§4 GO BACK"));
        back.setItemMeta(itemMeta);
        inv.setItem(18,back);

        showCoins = new ItemStack(Material.PLAYER_HEAD);
        itemMeta = showCoins.getItemMeta();
        int coins = CoinsAPI.getCoins(p.getName());
        itemMeta.setDisplayName(Color("You have " + coins + " Coins in your Inventory."));
        showCoins.setItemMeta(itemMeta);
        inv.setItem(3,showCoins);

        showAcc = new ItemStack(Material.GOLD_BLOCK);
        itemMeta = showAcc.getItemMeta();
        coins = CoinsAPI.getCoins(p.getName());
        Account acc = BankAccountAPI.getAccount(p.getName(),name).get(0);
        itemMeta.setDisplayName(Color("Coins: " + acc.balance));
        showAcc.setItemMeta(itemMeta);
        inv.setItem(5,showAcc);
    }
    public void open(){
        p.openInventory(inv);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        Inventory clicked = e.getClickedInventory();
        //ungewollte Fälle verhindern
        if(clicked == null) return;
        if(!e.getWhoClicked().equals(p)) return;
        if (clicked.equals(p.getInventory())){
            e.setCancelled(true);
            return;
        }
        if (!clicked.equals(inv))return;

        if (e.getAction().equals(InventoryAction.HOTBAR_SWAP)){
            e.setCancelled(true);
            return;
        }
        ItemStack stack = e.getCurrentItem();
        if (stack == null || stack.getType().equals(Material.AIR)){
            return;
        }
        e.setCancelled(true);

        if(stack.equals(depositCoins)){
            int coins = CoinsAPI.getCoins(p.getName());
            CoinsAPI.removeCoins(p.getName(),coins);
            BankAccountAPI.addCoins(p.getName(),coins,name);
            p.sendMessage("You deposited §2" + coins + " §f Coins.");
            p.closeInventory();
            AccountGUIInventory accGUI = new AccountGUIInventory(p,name,27);
            accGUI.setItems();
            accGUI.open();
        }
        if(stack.equals(removeCoins)){

            int coins = BankAccountAPI.getAccount(p.getName(),name).get(0).balance;
            CoinsAPI.addCoins(p.getName(),coins);
            BankAccountAPI.removeCoins(p.getName(),coins,name);
            p.sendMessage("You removed §4" + coins + " §f Coins.");
            p.closeInventory();
            ItemStack nuggets = new ItemStack(Material.GOLD_NUGGET,coins);
            p.getInventory().addItem(nuggets);
            AccountGUIInventory accGUI = new AccountGUIInventory(p,name,27);
            accGUI.setItems();
            accGUI.open();
        }
        if(stack.equals(exit)){
            p.closeInventory();
        }
        if(stack.equals(back)){
            p.closeInventory();
            BanksystemGUIInventory bankGUI = new BanksystemGUIInventory(p,"§6Banksystem",27);
            bankGUI.setItems();
            bankGUI.open();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        HandlerList.unregisterAll(this);
    }
    private String Color(String text){
        return ChatColor.translateAlternateColorCodes('§',text);
    }
}
