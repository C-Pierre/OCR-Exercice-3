package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.service.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    private AuthTokenFilter authTokenFilter;
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);

        authTokenFilter = new AuthTokenFilter();
        ReflectionTestUtils.setField(authTokenFilter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(authTokenFilter, "userDetailsService", userDetailsService);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenTokenValid() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String token = "valid.jwt.token";
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("john@test.com");

        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).username("john@test.com").build();
        when(userDetailsService.loadUserByUsername("john@test.com")).thenReturn(user);

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(user);
        verify(chain).doFilter(req, resp);
    }

    @Test
    void shouldNotAuthenticateWhenHeaderNull() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain).doFilter(req, resp);
    }

    @Test
    void shouldNotAuthenticateWhenHeaderMalformed() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Header qui ne commence pas par "Bearer "
        when(req.getHeader("Authorization")).thenReturn("Token abc.def");
        authTokenFilter.doFilterInternal(req, resp, chain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Header "Bearer " sans token
        when(req.getHeader("Authorization")).thenReturn("Bearer ");
        authTokenFilter.doFilterInternal(req, resp, chain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(chain, times(2)).doFilter(req, resp);
    }

    @Test
    void shouldNotAuthenticateWhenTokenInvalidOrUsernameOrUserDetailsNull() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        String token = "jwt.token";

        // Token invalide
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);
        authTokenFilter.doFilterInternal(req, resp, chain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Token valide mais username null
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(null);
        authTokenFilter.doFilterInternal(req, resp, chain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Token valide, username valide mais userDetails null
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("john@test.com");
        when(userDetailsService.loadUserByUsername("john@test.com")).thenReturn(null);
        authTokenFilter.doFilterInternal(req, resp, chain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(chain, times(3)).doFilter(req, resp);
    }

    @Test
    void shouldCatchRuntimeException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtils.validateJwtToken("token")).thenThrow(new RuntimeException("error"));

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain).doFilter(req, resp);
    }

    @Test
    void shouldCatchUnsupportedJwtException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtils.validateJwtToken("token")).thenThrow(new UnsupportedJwtException("unsupported"));

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain).doFilter(req, resp);
    }

    @Test
    void shouldCatchGenericException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String token = "any.jwt.token";
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenThrow(new IllegalArgumentException("unexpected"));

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain).doFilter(req, resp);
    }

    @Test
    void shouldProceedWhenAuthenticationAlreadySet() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("existing", null, List.of())
        );

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo("existing");
        verify(chain).doFilter(req, resp);
    }

    @Test
    void shouldAuthenticateAndCallSetDetails() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String token = "valid.jwt.token";
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("john@test.com");

        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).username("john@test.com").build();
        when(userDetailsService.loadUserByUsername("john@test.com")).thenReturn(user);

        authTokenFilter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(user);
        verify(chain).doFilter(req, resp);
    }
}
