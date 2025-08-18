package Logic;


public class UserData {
    private int id;
    private String name;
    private String lastname;
    private String password;


    public UserData(int id, String name, String lastname, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
    }

    public UserData() {
    }


    public int getId() {
        return id;
    }

    public String getPass() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastname;
    }



}