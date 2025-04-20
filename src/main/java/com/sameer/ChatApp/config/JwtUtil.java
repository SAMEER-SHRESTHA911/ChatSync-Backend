package com.sameer.ChatApp.config;

import com.sameer.ChatApp.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private  String jwtSecret;

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String extractUserName(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT token is expired");
        } catch (MalformedJwtException | SignatureException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }catch(Exception e){
            throw new IllegalArgumentException("Invalid JWT Token");
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUserName(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            return isValid;
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}
