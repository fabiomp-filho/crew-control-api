package br.com.crewcontrolapi.infra.security;

import br.com.crewcontrolapi.domain.entities.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    private JWTVerifier verifier;

    @PostConstruct
    private void init() {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm)
                .withIssuer("crew-control-api")  // Verifica se o issuer é o esperado
                .build();
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("crew-control-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String getUserLogin(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception exception) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String username = jwt.getSubject();
        String role = jwt.getClaim("role").asString(); // Assume que a claim é chamada "role"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
