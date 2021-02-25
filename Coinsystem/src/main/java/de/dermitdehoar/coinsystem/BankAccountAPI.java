package de.dermitdehoar.coinsystem;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountAPI {

    public static List<Account> getAccount(String name, String accName) {
        //alle Accounts eines Spielers bekommen
        try {
            PreparedStatement stat = MySQL.conn.prepareStatement("SELECT * FROM bankaccount WHERE player_name = ? AND name = ?");
            stat.setString(1, name);
            stat.setString(2, accName);
            ResultSet res = stat.executeQuery();
            List<Account> accounts = new ArrayList<Account>();
            while (res.next()) {
                int balance = res.getInt("balance");
                accounts.add(new Account(name, balance, accName));
            }
            if(accounts.size() > 0)return accounts;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return null;
    }

    public static List<Account> getAllAccounts(String name) {
        //alle Accounts eines Spielers bekommen
        try {
            PreparedStatement stat = MySQL.conn.prepareStatement("SELECT * FROM bankaccount WHERE player_name = ?");
            stat.setString(1, name);
            ResultSet res = stat.executeQuery();
            List<Account> accounts = new ArrayList<Account>();
            while (res.next()) {
                int balance = res.getInt("balance");
                String accName = res.getString("name");
                accounts.add(new Account(name, balance, accName));
            }
            return accounts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void createAccount(String name, int coins, String accName) {
        List<Account> accounts = getAllAccounts(name);
        Player p = Coinsystem.getInstance().getServer().getPlayer(name);
        if (accounts.size() < 3) {
            if (getAccount(name, accName) == null) {
                try {
                    PreparedStatement stat = MySQL.conn.prepareStatement("INSERT INTO bankaccount (player_name,balance,name) VALUES (?,?,?)");
                    stat.setString(1, name);
                    stat.setInt(2, coins);
                    stat.setString(3, accName);
                    stat.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                p.sendMessage("This Bankaccount already exists. Pls try again with a new name.");
            }
        }
        else{
            p.sendMessage("Pls delete a Bankaccount before you create a new one. You reached the max amount of 3 Bankaccounts");
        }
    }

    public static void removeAccount(String name, String accName) {
        List<Account> accounts = getAccount(name, accName);
        Player p = Coinsystem.getInstance().getServer().getPlayer(name);
        if (accounts != null) {
            PreparedStatement stat = null;
            for (Account account : accounts) {
                if (account.balance == 0) {
                    try {
                        stat = MySQL.conn.prepareStatement("DELETE FROM bankaccount WHERE player_name = ? AND name = ?");
                        stat.setString(2, accName);
                        stat.setString(1, name);
                        stat.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else {
                    p.sendMessage("You cant delete the Bankaccount because there is still money on it.");
                }
            }
        }
    }

    public static void addCoins(String name, int coins, String accName) {
        List<Account> acc = getAccount(name, accName);
        int balance = acc.get(0).balance;
        PreparedStatement stat = null;
        try {
            stat = MySQL.conn.prepareStatement("UPDATE bankaccount SET balance = ? WHERE name = ? AND player_name = ?");
            stat.setInt(1,coins+balance);
            stat.setString(2, accName);
            stat.setString(3, name);
            stat.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void removeCoins(String name, int coins, String accName) {
        List<Account> acc = getAccount(name, accName);
        int balance = acc.get(0).balance;
        PreparedStatement stat = null;
        if(balance >= coins){
        try {
            stat = MySQL.conn.prepareStatement("UPDATE bankaccount SET balance = ? WHERE name = ? AND player_name = ?");
            stat.setInt(1,balance-coins);
            stat.setString(2, accName);
            stat.setString(3, name);
            stat.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        }else {
            Player p = Coinsystem.getInstance().getServer().getPlayer(name);
            p.sendMessage("§4You cant remove more from the Account than is on it.");
        }
    }


    public static void transferCoins(Player pFrom, Player pTo, int coins, String fromAcc, String toAcc) {
        if (BankAccountAPI.getAccount(pFrom.getName(), fromAcc) == null){
            pFrom.sendMessage("§4 You dont have a Bankaccount named " + fromAcc);
            return;
        }
        if (BankAccountAPI.getAccount(pTo.getName(), toAcc) == null){
            pFrom.sendMessage("§4 The Account " + toAcc + " does not exist for Player " + pTo.getName());
            return;
        }
        if(coins < BankAccountAPI.getAccount(pFrom.getName(),fromAcc).get(0).balance){
        BankAccountAPI.removeCoins(pFrom.getName(),coins,fromAcc);
        BankAccountAPI.addCoins(pTo.getName(),coins,toAcc);
        }
        else{
            pFrom.sendMessage("§4 You want to transfer more coins than you have.");
        }
    }
    public static void interest(String name, String accName){
        List<Account> acc = getAccount(name, accName);
        int balance = acc.get(0).balance;
        PreparedStatement stat = null;
        try {
            stat = MySQL.conn.prepareStatement("UPDATE bankaccount SET balance = ? WHERE name = ? AND player_name = ?");
            stat.setInt(1,balance*105/100);
            stat.setString(2, accName);
            stat.setString(3, name);
            stat.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
