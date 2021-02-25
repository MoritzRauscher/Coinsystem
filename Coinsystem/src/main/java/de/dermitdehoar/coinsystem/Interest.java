package de.dermitdehoar.coinsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Interest {
    Date start;
    Interest(){
        start = new Date();
    }

    public void startInterest() {
            Date Interestday = new Date();
            long diff = Interestday.getTime() - start.getTime();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Interest is called. diff:" + diff);
            if(diff > 500000) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Interest is added. diff:" +diff);
                start = Interestday;
                PreparedStatement stat = null;
                List<Account> accounts = new ArrayList<Account>();
                try {
                    stat = MySQL.conn.prepareStatement("SELECT * FROM bankaccount");
                    ResultSet res = stat.executeQuery();
                    while (res.next()) {
                        String name = res.getString("player_name");
                        String accName = res.getString("name");
                        int balance = res.getInt("balance");
                        accounts.add(new Account(name, balance, accName));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                for(Account account : accounts)
                BankAccountAPI.interest(account.player_name,account.name);
            }
    }
}
