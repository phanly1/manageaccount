package com.manageaccount.manageaccount.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    // Đọc khóa bí mật và thời gian hết hạn từ application.properties
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Tạo JWT
    public String generateToken(Long accountId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("accountId", accountId);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject("BankManagementUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //xác nhận
    private Claims extractClaims(String token){ //claims là cặp khóa key-value chứa các thông tin và nằm trong payload của token
        try{
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build();
            return parser.parseClaimsJws(token).getBody();//giai ma token de xac thuc tính toàn vẹn Dl cũng như lấy ra payload(body-thong tin gui)
        }catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT has expired", e);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public Long getAccountIdFromToken(String token){
        try{
            Claims claims = extractClaims(token);

            return claims.get("accountId", Long.class);
        }catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }
}
