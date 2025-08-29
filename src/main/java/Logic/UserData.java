package Logic;


public class UserData {
    private final int id;
    private final String name;
    private final String lastname;
    private final String password;


    public UserData(int id, String name, String lastname, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
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