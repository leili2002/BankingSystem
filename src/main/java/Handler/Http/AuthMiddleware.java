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
        System.out.println("DEBUG: Authorization header = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("DEBUG: Missing or invalid Authorization header");
            unauthorized(exchange, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "
        System.out.println("DEBUG: Token received = [" + token + "]");

        try {
            // ✅ Correct parsing for JJWT 0.11.x+
            Claims claims = Jwts.parser()       // use parserBuilder()
                    .setSigningKey(SECRET_KEY)        // set the signing key
                    .build()                          // build the parser
                    .parseClaimsJws(token)            // parse the token
                    .getBody();                       // extract claims

            // Debug print all claims
            claims.forEach((k, v) -> System.out.println("DEBUG: Claim -> " + k + ": " + v));

            // Optionally, store claims in the exchange for later use
            exchange.setAttribute("claims", claims);

            // Token valid → continue to actual handler
            next.handle(exchange);

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            System.out.println("DEBUG: Token expired!");
            unauthorized(exchange, "Token expired");
        } catch (io.jsonwebtoken.SignatureException ex) {
            System.out.println("DEBUG: Invalid token signature!");
            unauthorized(exchange, "Invalid token signature");
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            System.out.println("DEBUG: Malformed token!");
            unauthorized(exchange, "Malformed token");
        } catch (Exception e) {
            System.out.println("DEBUG: Other token parsing error: " + e.getMessage());
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
