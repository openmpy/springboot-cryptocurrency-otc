package com.openmpy.ecommerce.global.jwt;

import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    public static final String BEARER_PREFIX = "Bearer ";
    private static final int TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 3;
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public String generateAccessToken(MemberEntity memberEntity) {
        return generateToken(memberEntity.getEmail());
    }

    public String getEmail(String accessToken) {
        return getSubject(accessToken);
    }

    private String generateToken(String subject) {
        var now = new Date();
        var exp = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .subject(subject)
                .signWith(SECRET_KEY)
                .issuedAt(now)
                .expiration(exp)
                .compact();
    }

    private String getSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            log.error("JwtException", e);
            throw e;
        }
    }
}
