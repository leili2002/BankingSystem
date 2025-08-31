package Logic;


public class UserData {
    private final int national_id;
    private final String name;
    private final String lastname;
    private final String password;


    public UserData(int id, String name, String lastname, String password) {
        this.national_id = id;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
    }


    public int getNational_id() {
        return national_id;
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