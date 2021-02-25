package de.dermitdehoar.coinsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    public static Connection conn ;

    public static void connect(){
        if(!isConnected()){
            try {
                conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/coins?autoReconnect=true", "root", "");
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL Connection succesful");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void disconnected(){
        if(isConnected()) {
            try {
                conn.close();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Disconnected from MySQL");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static boolean isConnected(){
        return conn != null;
    }

    public static void createDatabase() throws SQLException {
        conn.prepareStatement("CREATE TABLE IF NOT EXISTS bankaccount (balance INT(100), player_name VARCHAR(30))");
        conn.prepareStatement("CREATE TABLE IF NOT EXISTS player (coins INT(100), name VARCHAR(30))");
    }
}
