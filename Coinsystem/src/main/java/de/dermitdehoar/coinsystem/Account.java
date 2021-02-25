package de.dermitdehoar.coinsystem;

public class Account {
    public int balance;
    public String name;
    public String player_name;

    public Account(String name, int i, String accName) {
        this.name = accName;
        this.player_name = name;
        this.balance = i;
    }
}
