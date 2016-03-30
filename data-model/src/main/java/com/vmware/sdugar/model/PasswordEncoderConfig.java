package com.vmware.sdugar.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * Created by sourabhdugar on 3/28/16.
 */
@Configuration
public class PasswordEncoderConfig {

    private static Logger log = LoggerFactory.getLogger(PasswordEncoderConfig.class);

    private static final String KEY = "mysecretkey";

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String createJWT(String id, String issuer, String subject, String userPassword) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //User password signing key
        byte[] userKeyBytes = DatatypeConverter.parseBase64Binary(userPassword);
        Key userSigningKey = new SecretKeySpec(userKeyBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, userSigningKey)
                .claim("ROLE", "USER_ROLE");

        //if it has been specified, let's add the expiration
        //if (ttlMillis >= 0) {
        long expMillis = nowMillis + 100000;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
        //}

        //Builds the JWT and serializes it to a compact, URL-safe string
        String payload = builder.compact();

        return payload;
    }

    public static void parseJWT(String jwt, String userPassword) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        //Claims claims = Jwts.parser().setSigningKey(
        //        DatatypeConverter.parseBase64Binary(userPassword)).parseClaimsJws(jwt).getBody();
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(userPassword))
                .parseClaimsJws(jwt).getBody();

        log.info("ID: " + claims.getId());
        log.info("Subject: " + claims.getSubject());
        log.info("Issuer: " + claims.getIssuer());
        log.info("Expiration: " + claims.getExpiration());
        log.info("Role: {}", claims.get("ROLE"));
    }
}
