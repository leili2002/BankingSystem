package Handler.Dto;

public class InputAccount {
    public String name;
    public String lastname;
    float balance;
    String type;


    public InputAccount(String name, String lastname,String type,float balance) {
        this.name = name;
        this.lastname = lastname;
        this.type=type;
        this.balance=balance;
    }

    // Getters and setters (optional, Gson can work with public fields)
    public String get_name() {
        return name;
    }

    public String get_last_name() {
        return lastname;
    }

    public String get_type() {
        return type;
    }
    public float getBalance(){
    return balance;}

}
