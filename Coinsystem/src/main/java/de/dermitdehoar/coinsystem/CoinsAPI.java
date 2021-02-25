package de.dermitdehoar.coinsystem;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoinsAPI {

    public static int getCoins(String name){
        try {
            PreparedStatement stat = MySQL.conn.prepareStatement("SELECT * FROM player WHERE name = ?");
            stat.setString(1, name);
            ResultSet res = stat.executeQuery();
            while(res.next()){
                return res.getInt("coins");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
    public static void setCoins(String name, int coins){
        if (getCoins(name) == -1) {
            try {
                PreparedStatement stat = MySQL.conn.prepareStatement("INSERT INTO player (coins,name) VALUES (?,?)");
                stat.setInt(1,coins);
                stat.setString(2,name);
                stat.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else{
            PreparedStatement stat = null;
            try {
                stat = MySQL.conn.prepareStatement("UPDATE player SET coins = ? WHERE name = ?");
                stat.setInt(1,coins);
                stat.setString(2,name);
                stat.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static void addCoins(String name, int coins){
        setCoins(name, coins + getCoins(name));
    }
    public  static void removeCoins(String name, int coins){
        setCoins(name, getCoins(name) - coins);
    }
}
