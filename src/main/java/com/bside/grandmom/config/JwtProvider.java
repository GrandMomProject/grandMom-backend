package com.bside.grandmom.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;

@Service
public class JwtProvider {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
    JWT 생성
    @param userNo
    @return String
     */
    public String createJwt(Long userNo){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userNo",userNo)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(key)
                .compact();
    }

    public String createRepJwt(int repInx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("repInx",repInx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(key)
                .compact();
    }

    /**
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    /**
    JWT에서 userNo 추출
    @return int
     */
    public Long getUserNo() throws Exception {
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new Exception();
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new Exception();
        }

        // 3. userIdx 추출
        return claims.getBody().get("userNo",Long.class);  // jwt 에서 userIdx를 추출합니다.
    }
    public long validate(String jwt) {
        long subject = 0L;
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            subject = Long.parseLong(claims.getSubject());
        } catch (Exception exception){
            exception.printStackTrace();
            return 0L;
        }
        return subject;
    }


}
