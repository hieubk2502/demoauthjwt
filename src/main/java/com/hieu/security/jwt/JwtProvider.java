package com.hieu.security.jwt;

import com.hieu.security.userprincal.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component// khi chay tu dong load qua
// tao chuoi token
public class JwtProvider {
    // ghi ra 1 file va doc file do
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private String jwtSecret="hieutranminhbk@gmail.com";
    // set time cho chuoi token
    private int jwtException =86400;

    public String creatToken(Authentication authentication){
        // lay user hien tai thong qua user princal -co che security
        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();
        // chuoi token
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date(new Date().getTime()+jwtException))
                //chuan ma hoa
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();//dong lai
    }
    //    check xem token ton tai k
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        }catch (SignatureException e){
            logger.error("Ivalid JWT signature->Message:{}",e);// khong ton tai
        }
        catch (MalformedJwtException e){
            logger.error("the token invalid format",e);
        }
        catch (UnsupportedJwtException e){
            logger.error("unsupported jwt token ->Messgae: {}",e);
        }
        catch (ExpiredJwtException e){
            logger.error("Expired jwt token -> message:{})",e);
        }
        catch (IllegalArgumentException e){
            logger.error("jwt claim string is empty ->message:{}",e);
        }
        return false;
    }
    // tim user trong token
    public String getUserNameFromToken(String token){
        String userName = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody().getSubject();
        return userName;
    }

}
