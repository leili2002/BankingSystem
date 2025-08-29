package Logic;

import Repository.TransactionsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountData {
    private final UUID serial;
    private final int id;
    private final String name;
    private final String lastname;
    private final String type;
     private float balance;

    public AccountData(UUID serial, int id, String name, String lastname, String type, float balance) {
        this.serial = serial;
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.lastname = lastname;
        this.type = type;
    }

    public void setBalance(float newBalance) {
        this.balance = newBalance;
    }

    public  UUID getSerial(){return serial;}

    public  int getId(){
        return id;
    }

    public  float getBalance(){
        return balance;
    }

    public  String getName(){
        return name;
    }

    public  String getLastName(){
        return lastname;
    }

    public String getType(){return type;}

    public void updateBalance(int newAmount) {
        if (newAmount > 0) {
            balance += newAmount;
        } else {
            balance -= newAmount;
        }
    }

    }


