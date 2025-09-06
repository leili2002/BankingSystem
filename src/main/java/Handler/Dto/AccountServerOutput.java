package Handler.Dto;

public class AccountServerOutput {
    public String name;
    public String last_name;
    String type;
    float balance;

    // Optional: constructor for easier testing
    public AccountServerOutput(String name, String last_name,String type,float balance) {
        this.name = name;
        this.last_name = last_name;
        this.type=type;
        this.balance=balance;

    }

    // Getters and setters (optional, Gson can work with public fields)
    public String get_name() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }
    public String getType() {
        return type;
    }
    public float get_balance() {
        return balance;
    }

}
