package com.smartbee.crm.auth.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String SPRING_SECURITY_ROLE_CLAIMS = "rol";

    private String singingKey;

    public UserAuthorizationFilter(final AuthenticationManager authenticationManager, String singingKey) {
        super(authenticationManager);
        this.singingKey = singingKey;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.isEmpty(token)) {
            final Claims parsedToken = getTokenBody(token);

            String username = getUsername(parsedToken);
            final Set<SimpleGrantedAuthority> authorities = getAuthorities(parsedToken);

            if (!StringUtils.isEmpty(username)) {
                final Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * retrieve role authorities from token
     *
     * @param claims
     * @return
     */
    @SuppressWarnings("unchecked")
    private Set<SimpleGrantedAuthority> getAuthorities(final Claims claims) {
        return ((Collection<String>) claims.get(SPRING_SECURITY_ROLE_CLAIMS))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    /**
     * retrieve username from token
     *
     * @param claims
     * @return username
     */
    private String getUsername(final Claims claims){
        return claims.getSubject();
    }

    /**
     * parse token to {@link io.jsonwebtoken.Claims}
     *
     * @param token
     * @return parsed token
     */
    private Claims getTokenBody(final String token){
        String tokenBody = token.replace(UserAuthenticationFilter.SPRING_SECURITY_TOKEN_PREFIX, "");
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(singingKey.getBytes()))
                .build()
                .parseClaimsJws(tokenBody)
                .getBody();
    }
}
