package Logic;

import Repository.TransactionsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private  UUID serial;
    private  int id;
    private  String name;
    private  String lastname;
    private  String type;
     private float balance;
    ArrayList<Transaction> transaction = new ArrayList<>();

    public Account(String serial, int id, String name, String lastname, String type, float balance) {
        this.serial = UUID.randomUUID();
        this.id = id;
        this.balance = balance;
        this.name = name;
        this.lastname = lastname;
        this.type = type;
    }

    public Account( UUID serial, Object id, String name, String lastname, String gettype, float getbalance) {
    }

    public void setbalance(float newbalance) {
        this.balance = newbalance;
    }

    public  UUID getserial(){return serial;}

    public  int getid(){
        return id;
    }

    public  float getbalance(){
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

    public int AddTransaction(Transaction transaction) {
      return  TransactionsRepository.AddTransaction(transaction);
    }

    public List<Transaction> printTransactions(int id_account) {

      return   TransactionsRepository.printTransactions(id_account);
    }

    }


