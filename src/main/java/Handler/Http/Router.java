package Handler.Http;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, String> routes = new HashMap<>();
    private final Gson gson = new Gson();

    public Router() {
        // JSON API endpoints
        routes.put("/home", gson.toJson(Map.of("message", "Welcome to the bank site!")));
        routes.put("/menu", gson.toJson(Map.of(
                "options", new String[]{"Sign Up", "Log In", "Admin Login"}
        )));
        routes.put("/signup", gson.toJson(Map.of("message", "Sign-up successful")));
        routes.put("/login", gson.toJson(Map.of("message", "Login successful")));
        routes.put("/admin", gson.toJson(Map.of("message", "Admin login successful")));
    }

    public String route(String path, String method) {
        System.out.println("➡️ Request: " + method + " " + path);
        return routes.getOrDefault(path, gson.toJson(Map.of("error", "404 Not Found")));
    }
}
