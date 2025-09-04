package Logic;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class TokenUtil {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("mysecretmysecretmysecretmysecret".getBytes());

    public static String generateToken(int national_id, String name) {
        return Jwts.builder()
                .subject(String.valueOf(national_id))
                .claim("getName", name)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY)
                .compact();
    }
}
