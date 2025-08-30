package Handler.Http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerApp {

    private HttpServer server;
    private final Router router;

    public HttpServerApp(int port) throws IOException {
        this.router = new Router();
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.createContext("/", this::handleRequest);
        this.server.setExecutor(null);
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        // Allow CORS
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Get JSON response from router
        String response = router.route(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        // Send headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public void start() {
        System.out.println("✅ HTTP server started at http://localhost:" + server.getAddress().getPort());
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("🛑 Server stopped.");
    }

    public static void main(String[] args) throws IOException {
        HttpServerApp app = new HttpServerApp(8080);
        app.start();
    }
}
