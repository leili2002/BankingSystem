package Logic.Dto;


public class LoginRequest {
    public int national_id;
    public String password;

    // Optional: constructor for easier testing
    public LoginRequest(int national_id, String password) {
        this.national_id = national_id;
        this.password = password;
    }

    // Getters and setters (optional, Gson can work with public fields)
    public int getNational_id() {
        return national_id;
    }

    public void setNational_id(int national_id) {
        this.national_id = national_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
