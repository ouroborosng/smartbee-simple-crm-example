package com.smartbee.crm.auth.config;

import com.smartbee.crm.auth.filter.UserAuthenticationFilter;
import com.smartbee.crm.auth.filter.UserAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.secret}")
    private String singingKey;

    private final UserDetailsService jwtUserDetailsService;

    public WebSecurityConfig(UserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/public").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/client/**").hasAuthority("VIEW_CLIENT")
                .antMatchers(HttpMethod.POST, "/client/**").hasAuthority("CREATE_CLIENT")
                .antMatchers(HttpMethod.PUT, "/client/**").hasAuthority("MODIFY_CLIENT")
                .antMatchers(HttpMethod.DELETE, "/client/**").hasAuthority("DELETE_CLIENT")
                .antMatchers(HttpMethod.GET, "/company/**").hasAuthority("VIEW_COMPANY")
                .antMatchers(HttpMethod.POST, "/company/**").hasAuthority("CREATE_COMPANY")
                .antMatchers(HttpMethod.PUT, "/company/**").hasAuthority("MODIFY_COMPANY")
                .antMatchers(HttpMethod.DELETE, "/company/**").hasAuthority("DELETE_COMPANY")
                .anyRequest().authenticated()
                .and()
                .addFilter(new UserAuthenticationFilter(authenticationManager(), singingKey))
                .addFilter(new UserAuthorizationFilter(authenticationManager(), singingKey))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
