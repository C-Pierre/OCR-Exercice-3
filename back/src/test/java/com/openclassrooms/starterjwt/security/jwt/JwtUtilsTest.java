package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        // Injection de jwtSecret via réflexion
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, "FYvfkEBYUt39SSosBnrhFcNivCLgvrq4EONxpvcIgR3F7Q7cdB0MryMiB1IoRiNusplHcE8jC4c5KMMTtuVm5Fc478845877a94d8d5e952f27ca51d3348ee779136b93ad259c533d54c5cbba5c");

        // Injection de la durée d'expiration
        Field expField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expField.setAccessible(true);
        expField.set(jwtUtils, 1000 * 60 * 10); // 10 minutes
    }

    // ---------- Token valide ----------

    @Test
    void generateJwtToken_shouldReturnValidToken() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("john@test.com")
                .password("secret")
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);

        String token = jwtUtils.generateJwtToken(auth);

        assertThat(token).isNotNull();
        assertThat(jwtUtils.validateJwtToken(token)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo("john@test.com");
    }

    // ---------- Cas invalides ----------

    @Test
    void validateJwtToken_shouldReturnFalse_forNullOrEmptyToken() {
        assertThat(jwtUtils.validateJwtToken(null)).isFalse();
        assertThat(jwtUtils.validateJwtToken("")).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forMalformedToken() {
        assertThat(jwtUtils.validateJwtToken("abc.def")).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forUnsupportedToken() {
        assertThat(jwtUtils.validateJwtToken("unsupported.token.example")).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forTamperedToken() {
        UserDetailsImpl user = UserDetailsImpl.builder().username("john@test.com").build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);

        String token = jwtUtils.generateJwtToken(auth);
        String tamperedToken = token + "123";

        assertThat(jwtUtils.validateJwtToken(tamperedToken)).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forExpiredToken() throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        String secret = (String) secretField.get(jwtUtils);

        String expiredToken = Jwts.builder()
                .setSubject("john@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10_000))
                .setExpiration(new Date(System.currentTimeMillis() - 5_000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        assertThat(jwtUtils.validateJwtToken(expiredToken)).isFalse();
    }

    // ---------- Couverture Exceptions spécifiques ----------

    @Test
    void validateJwtToken_shouldReturnFalse_forSignatureException() throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        String secret = (String) secretField.get(jwtUtils);

        // Création d’un token valide
        UserDetailsImpl user = UserDetailsImpl.builder().username("john@test.com").build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        String token = jwtUtils.generateJwtToken(auth);

        // Tampering pour déclencher SignatureException
        String tamperedToken = token.substring(0, token.length() - 1) + "x";

        assertThat(jwtUtils.validateJwtToken(tamperedToken)).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forUnsupportedJwtException() {
        String unsupported = "unsupported.jwt.token";
        assertThat(jwtUtils.validateJwtToken(unsupported)).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forMalformedJwtException() {
        String malformed = "malformed.jwt";
        assertThat(jwtUtils.validateJwtToken(malformed)).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forExpiredJwtException() throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        String secret = (String) secretField.get(jwtUtils);

        String expiredToken = Jwts.builder()
                .setSubject("john@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20_000))
                .setExpiration(new Date(System.currentTimeMillis() - 10_000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        assertThat(jwtUtils.validateJwtToken(expiredToken)).isFalse();
    }

    @Test
    void validateJwtToken_shouldReturnFalse_forIllegalArgumentException() {
        // Par exemple token null ou vide → couvert par validateJwtToken déjà
        assertThat(jwtUtils.validateJwtToken(" ")).isFalse();
    }
}
