package Handler.Dto;

public class LoginServerOutput {
    public String name;
    public String token;

    // Optional: constructor for easier testing
    public LoginServerOutput(String name, String token) {
        this.name = name;
        this.token = token;
    }

    // Getters and setters (optional, Gson can work with public fields)
    public String get_name() {
        return name;
    }

    public String get_token() {
        return token;
    }

}

