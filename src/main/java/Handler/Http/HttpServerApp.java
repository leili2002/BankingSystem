package Handler.Http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class HttpServerApp {

    private final HttpServer server;
    private final Router router;
    private final Path webRoot = Paths.get("web").toAbsolutePath().normalize();

    public HttpServerApp(int port) throws IOException {
        this.router = new Router();
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        // API context
        this.server.createContext("/", this::handleRequest);
        // Static files context
        this.server.createContext("/web", this::handleStaticFiles);

        this.server.setExecutor(null); // default executor
    }

    private void handleStaticFiles(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath().replace("/web", "");
            if (path.isEmpty() || path.equals("/")) path = "index.html";
            if (path.startsWith("/")) path = path.substring(1);

            Path filePath = webRoot.resolve(path).normalize();

            // Prevent path traversal
            if (!filePath.startsWith(webRoot) || !Files.exists(filePath)) {
                sendResponse(exchange, 404, "File not found", "text/plain");
                return;
            }

            byte[] bytes = Files.readAllBytes(filePath);
            String contentType = "text/html";
            if (path.endsWith(".css")) contentType = "text/css";
            else if (path.endsWith(".js")) contentType = "application/javascript";

            sendResponse(exchange, 200, bytes, contentType);

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal Server Error", "text/plain");
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        try {
            // CORS headers
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // Delegate to router
            router.route(exchange);

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\":\"Internal Server Error\"}", "application/json");
        }
    }

    // Helper method for sending String responses
    private void sendResponse(HttpExchange exchange, int statusCode, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Overload for byte[] responses (static files)
    private void sendResponse(HttpExchange exchange, int statusCode, byte[] bytes, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
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
