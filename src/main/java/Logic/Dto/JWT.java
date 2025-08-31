package Logic.Dto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class TokenUtil {
    private static final String SECRET_KEY = "mysecret"; // keep safe!

    public static String generateToken(int national_id, String name) {
        return Jwts.builder()
                .setSubject(String.valueOf(national_id))
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
