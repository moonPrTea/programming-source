package dev.moon.programming.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  private final String secretKey;
  private final long accessTokenTtlMs;
  private final long refreshTokenTtlMs;
  private final String jwtCookieName;

  public JwtService(
          @Value("${application.security.jwt.secret-key}")
          String secretKey,
          @Value("${application.security.jwt.expiration}")
          long accessTokenTtlMs,
          @Value("${application.security.jwt.refresh-token.expiration}")
          long refreshTokenTtlMs,
          @Value("${application.security.jwt.cookie-name}")
          String jwtCookieName
  ) {
    this.secretKey = secretKey;
    this.accessTokenTtlMs = accessTokenTtlMs;
    this.refreshTokenTtlMs = refreshTokenTtlMs;
    this.jwtCookieName = jwtCookieName;
  }

  public String getUsername(String token) {
    return getClaim(token, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }
  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return createToken(extraClaims, userDetails, accessTokenTtlMs);
  }

  private String createToken(Map<String, Object> claims, UserDetails userDetails, long ttl) {
    long now = System.currentTimeMillis();

    return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(now))
            .expiration(new Date(now + ttl))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
  }

  public boolean checkTokenValid(String token, UserDetails userDetails) {
    String username = getUsername(token);

    return username.equals(userDetails.getUsername()) && !checkTokenExpired(token);
  }

  private boolean checkTokenExpired(String token) {
    return getAllClaims(token)
            .getExpiration()
            .before(new Date());
  }

  private <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
    final Claims claims = getAllClaims(token);

    return claimsFunction.apply(claims);
  }

  private Claims getAllClaims(String token) {
    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);

    return Keys.hmacShaKeyFor(keyBytes);
  }

  public ResponseCookie createJwtCookie(String jwt) {
    return ResponseCookie.from(jwtCookieName, jwt)
            .path("/")
            .maxAge(Duration.ofHours(24))
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .build();
  }

  public ResponseCookie getEmptyJwtCookie() {
    return ResponseCookie.from(jwtCookieName, "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .build();
  }

  public String getJwtFromCookie(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookieName);

    return cookie != null ? cookie.getValue() : null;
  }
}
