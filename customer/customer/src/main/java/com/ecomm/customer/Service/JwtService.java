package com.ecomm.customer.Service;

import com.ecomm.customer.Exceptions.InvalidTokenException;
import com.ecomm.customer.Modal.Token;
import com.ecomm.customer.Repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private String SECRET = "euwhiuhciuhurevhiuerhvuirvhhhhhhhhhhhhhhhhhhhhhhhhhhhqbbriuvuhvureqqqqqqqqqqqhoqurvhoqhvo";

    @Autowired
    private TokenRepository tokenRepository;
//    @Value("${security.jwt.secret-key}")
//    private String SECRET;

//    @Value("${security.jwt.expiration-time}")
//    private long jwtExpiration;

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public <T> T extractClaims(String token,Function<Claims,T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }


    public boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }
    //validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
//
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateAccessToken(String username, String role) {
        return generateToken(username,role,new HashMap<>(),60*60*1000);
    }

    public String refreshAccessToken(String username, String role) {
        return generateToken(username,role,new HashMap<>(),30*24*60*60*1000);
    }

    public String generateToken(String username,String role,Map<String,Object> claims,long expirationTime){
//        Map<String,Object> claims = new HashMap<>();
//        claims.put("role",role);
        return Jwts.builder()
                .claims()
                .add(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .subject(username)
                .and()
                .signWith(getKey())
                .compact();
    }
    public SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
