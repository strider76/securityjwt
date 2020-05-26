package com.example.security.service.impl;

import com.example.security.exception.AccessDeniedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

@Service
public class AuthenticationService {

    //Token Expiration time (ms) 60 * 60 * 1000 = 3600000 1 hora
    private static final long EXPIRATIONTIME = 3600000L;
    private static final String PREFIX = "Bearer";

    //clave para firmar/descifar JWT
    @Value("${spring.app.jwtSecret}")
    private String SIGNINGKEY;

    //Creamos Token JWT y lo añadimos al Response en el header "Authorization"(Bearer tokenjwt"
    // y damos access-control para que Javascript pueda utilizar el token en front
    public void addToken(HttpServletResponse response, String userName) {
        String jwt = Jwts.builder()
                        .setSubject(userName)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                        .signWith(SignatureAlgorithm.HS512, SIGNINGKEY)
                        .compact();
        response.addHeader("Authorization", PREFIX + " " + jwt);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "180");
    }

    //en las llamadas que se hagan, obtenemos token del header->Authorization
    // si vemos que hay usuario en el token, entonces autenticamos al usuario para usar nuestro api
    public Authentication getAuthentication (HttpServletRequest request) throws AccessDeniedException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                String user = Jwts.parser()
                        .setSigningKey(SIGNINGKEY)
                        .parseClaimsJws(token.replace(PREFIX, ""))
                        .getBody()
                        .getSubject();

                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());
                }
            } catch (ExpiredJwtException expired) {
                throw new AccessDeniedException("Token expirado, por favor realice login nuevamente");
            } catch (SignatureException signature) {
                throw new AccessDeniedException("Token no válido");
            }
        }
        return null;
    }
}
