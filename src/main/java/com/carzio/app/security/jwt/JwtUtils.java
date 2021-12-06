package com.carzio.app.security.jwt;

import com.carzio.app.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${carzio.app.jwtSecret}")
    private String jwtSecret;

    @Value("${carzio.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${carzio.app.refreshExpirationDateInMs}")
    private int refreshExpirationDateInMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername(), jwtExpirationMs);
    }

    private String generateToken(String subject, int expirationTime) {
        return Jwts.builder()
                .setSubject((subject))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(HttpServletRequest request, String subject) {
        return generateToken(subject, refreshExpirationDateInMs);
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
