package com.smartbee.crm.auth.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_TOKEN_PREFIX = "Bearer";

    private static final long EXPIRATION = 3600L;
    private static final String SPRING_SECURITY_TOKEN_TYPE = "JWT";
    private static final String SPRING_SECURITY_ISSUER = "smartbee-crm-api";
    private static final String SPRING_SECURITY_AUDIENCE = "smartbee-aud";

    private String singingKey;
    private final AuthenticationManager authManager;

    public UserAuthenticationFilter(AuthenticationManager authManager, String singingKey) {
        super.setFilterProcessesUrl("/auth/login");
        this.authManager = authManager;
        this.singingKey = singingKey;
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            TypeReference<HashMap<String,Object>> typeRef
                    = new TypeReference<HashMap<String,Object>>() {};
            final HashMap<String, Object> user = new ObjectMapper().readValue(request.getInputStream(), typeRef);
            final Object username = user.get(SPRING_SECURITY_FORM_USERNAME_KEY);
            final Object password = user.get(SPRING_SECURITY_FORM_PASSWORD_KEY);
            final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

            return authManager.authenticate(authentication);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        final User user = (User) authResult.getPrincipal();
        final Collection<String> permissions = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(singingKey.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SPRING_SECURITY_TOKEN_TYPE)
                .setIssuer(SPRING_SECURITY_ISSUER)
                .setAudience(SPRING_SECURITY_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .claim("rol", permissions)
                .compact();
        String tokenWithPrefix = String.format("%s %s", SPRING_SECURITY_TOKEN_PREFIX, token);

        response.addHeader(HttpHeaders.AUTHORIZATION, tokenWithPrefix);
    }
}
