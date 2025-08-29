package Repository;

import java.util.UUID;

public class AccountModel {
    private UUID serial;
    private int Repo_id;
    private String name;
    private String lastname;
    private String type;
    private float balance;

    public AccountModel(UUID serial, int Repo_id, String name, String lastname, String type, Float balance) {
        this.serial = serial;
        this.Repo_id = Repo_id;
        this.name = name;
        this.lastname = lastname;
        this.type = type;
        this.balance = balance;
    }

    public int getRepo_id() {
        return Repo_id;
    }

    public void Repo_id(int Repo_id) {
        this.Repo_id = Repo_id;
    }

    public String get_Name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public float getbalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public UUID getUuid() {
        return serial;
    }

    public int getId() {
        return getRepo_id();
    }
}


