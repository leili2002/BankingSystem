package Handler.Http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, Handler> routes = new HashMap<>();

    public Router() {
        routes.put("/login", new LoginHandler());
        routes.put("/signup", new SignupHandler());
        routes.put("/admin",new AdminHandler());
        // Add more handlers here, e.g., /admin, /menu, etc.
    }

    public void route(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Handler handler = routes.get(path);

        if (handler != null) {
            handler.handle(exchange);
        } else {
            String error = "{\"error\":\"404 Not Found\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, error.getBytes().length);
            exchange.getResponseBody().write(error.getBytes());
            exchange.getResponseBody().close();
        }
    }
}
