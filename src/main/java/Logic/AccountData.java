package Logic;
import java.util.UUID;

public class AccountData {
    private  UUID serial;
    private  int id;
    private  String name;
    private  String lastname;
    private  String type;
     private float balance;

    // No-args constructor for Gson
    public AccountData() {
        this.serial = UUID.randomUUID();
        this.id = 0; // will be set by DB
    }

    // Constructor for creating new accounts in code
    public AccountData(String name, String lastname, String type, float balance) {
        this.name = name;
        this.lastname = lastname;
        this.type = type;
        this.balance = balance;
        this.serial = UUID.randomUUID();
        this.id = 0;
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


