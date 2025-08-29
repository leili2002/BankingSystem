
package Repository;

public class UserModel {
    private int Repon_national_id; // database ID
    private String name;
    private String lastname;
    private String password;

    public UserModel(int id, String name, String lastName, String pass) {
this .Repon_national_id=id;
this.name=name;
this.lastname=lastName;
this.password=pass;
    }

    public int getRepon_national_id() {
        return Repon_national_id;
    }

    public void setRepon_national_id(int repon_national_id) {
        this.Repon_national_id = repon_national_id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
