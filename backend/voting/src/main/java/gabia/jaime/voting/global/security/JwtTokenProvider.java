package gabia.jaime.voting.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    public boolean validate(final String token, final String username, final String secretKey) {
        String usernameByToken = getUsername(token, secretKey);
        return usernameByToken.equals(username) && !isTokenExpired(token, secretKey);
    }

    public String generateAccessToken(final String username, final String secretKey, final long expiredTimeMs) {
        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + expiredTimeMs);

        return Jwts.builder()
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(getSignKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(final String secretKey) {
        final byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenExpired(final String token, final String secretKey) {
        final Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    private Claims extractClaims(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(getSignKey(secretKey))
                .build().parseClaimsJws(token).getBody();
    }

    public String getUsername(String token, String secretKey) {
        return extractClaims(token, secretKey).get("username", String.class);
    }

}

