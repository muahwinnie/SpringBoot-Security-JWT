package com.lorem.JavaSecurityJWT.config;

import com.lorem.JavaSecurityJWT.user.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {

    @Autowired
    private AuthenticationProvider provider;

    @Autowired
    private jwtAuthentificationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(SessionManag -> SessionManag.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider)
                .addFilterBefore(filter , UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authconfig -> {
                            authconfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                            authconfig.requestMatchers(HttpMethod.POST, "/auth/Register").permitAll();
                            authconfig.requestMatchers("/error").permitAll();
                            authconfig.requestMatchers(HttpMethod.GET,"/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
                            authconfig.requestMatchers(HttpMethod.POST,"/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());
                            authconfig.anyRequest().denyAll();
                        }
                );
        return http.build();
    }
}
