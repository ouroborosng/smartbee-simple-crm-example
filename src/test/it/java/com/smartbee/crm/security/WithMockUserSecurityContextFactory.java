package com.smartbee.crm.security;

import com.smartbee.crm.auth.JwtUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    private final JwtUserDetailsService userDetailsService;

    public WithMockUserSecurityContextFactory(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockUser withMockUser) {
        final String username = withMockUser.username();
        Assert.hasLength(username, "username must be non-empty String");
        final String password = withMockUser.password();
        Assert.hasLength(username, "password must be non-empty String");

        final UserDetails principal = userDetailsService.loadUserByUsername(username);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(principal,
                principal.getPassword(), principal.getAuthorities());
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
