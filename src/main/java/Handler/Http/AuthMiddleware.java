package Handler.Http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

public class AuthMiddleware implements Handler {

    private final Handler next;
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "mysecretmysecretmysecretmysecret".getBytes() // must be ≥32 bytes
    );

    public AuthMiddleware(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            unauthorized(exchange, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // ✅ Correct parsing for JJWT 0.11.x+
            Claims claims = Jwts.parser()       // use parserBuilder()
                    .setSigningKey(SECRET_KEY)        // set the signing key
                    .build()                          // build the parser
                    .parseClaimsJws(token)            // parse the token
                    .getBody();                       // extract claims

            // Optionally, store claims in the exchange for later use
            exchange.setAttribute("claims", claims);

            // Token valid → continue to actual handler
            next.handle(exchange);

        } catch (Exception e) {
            unauthorized(exchange, "Invalid or expired token");
        }
    }

    private void unauthorized(HttpExchange exchange, String message) throws IOException {
        String response = "{\"error\":\"" + message + "\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(401, response.getBytes(StandardCharsets.UTF_8).length);
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().close();
    }
}
