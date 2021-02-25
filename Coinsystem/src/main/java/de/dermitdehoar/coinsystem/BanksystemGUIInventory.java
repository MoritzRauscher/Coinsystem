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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BanksystemGUIInventory implements Listener {
    private Player p;
    private String name;
    private Inventory inv;
    private ItemStack showKonto;
    private ItemStack account1;
    private ItemStack account2;
    private ItemStack account3;
    private ItemStack exit;

    public BanksystemGUIInventory(Player p, String name, int slots){
        this.p = p;
        this.name = name;
        inv = Bukkit.createInventory(p,slots,name);
        Coinsystem.getInstance().getServer().getPluginManager().registerEvents(this,Coinsystem.getInstance());
    }
    public void setItems() {
        showKonto = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = showKonto.getItemMeta();
        int coins = CoinsAPI.getCoins(p.getName());
        itemMeta.setDisplayName(Color("§2 You have " + coins + " Coins in your Inventory."));
        showKonto.setItemMeta(itemMeta);
        inv.setItem(4,showKonto);

        exit = new ItemStack(Material.RED_CONCRETE);
        itemMeta = exit.getItemMeta();
        itemMeta.setDisplayName(Color("§4EXIT"));
        exit.setItemMeta(itemMeta);
        inv.setItem(26,exit);

        List<Account> accounts = BankAccountAPI.getAllAccounts(p.getName());
        if(accounts.size()>0) {
            account1 = new ItemStack(Material.GREEN_CONCRETE);
            itemMeta = account1.getItemMeta();
            itemMeta.setDisplayName(Color(accounts.get(0).name));
            account1.setItemMeta(itemMeta);
            inv.setItem(10, account1);
        }
        if (accounts.size()>1) {
            account2 = new ItemStack(Material.BLUE_CONCRETE);
            itemMeta = account2.getItemMeta();
            itemMeta.setDisplayName(Color(accounts.get(1).name));
            account2.setItemMeta(itemMeta);
            inv.setItem(13, account2);
        }
        if (accounts.size()>2) {
            account3 = new ItemStack(Material.PINK_CONCRETE);
            itemMeta = account3.getItemMeta();
            itemMeta.setDisplayName(Color(accounts.get(2).name));
            account3.setItemMeta(itemMeta);
            inv.setItem(16, account3);
        }
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

        List<Account> accounts = BankAccountAPI.getAllAccounts(p.getName());
        if(stack.equals(account1)){
            p.closeInventory();
            AccountGUIInventory accGUI = new AccountGUIInventory(p,accounts.get(0).name,27);
            accGUI.setItems();
            accGUI.open();
        }
        if(stack.equals(account2)){
            p.closeInventory();
            AccountGUIInventory accGUI = new AccountGUIInventory(p,accounts.get(1).name,27);
            accGUI.setItems();
            accGUI.open();
        }
        if(stack.equals(account3)){
            p.closeInventory();
            AccountGUIInventory accGUI = new AccountGUIInventory(p,accounts.get(2).name,27);
            accGUI.setItems();
            accGUI.open();
        }
        if(stack.equals(exit)){
            p.closeInventory();
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
