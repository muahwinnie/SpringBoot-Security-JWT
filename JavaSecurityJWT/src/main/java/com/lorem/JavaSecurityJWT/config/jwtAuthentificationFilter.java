package com.lorem.JavaSecurityJWT.config;

import com.lorem.JavaSecurityJWT.user.User;
import com.lorem.JavaSecurityJWT.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class jwtAuthentificationFilter extends OncePerRequestFilter {

    @Autowired
    private jwtService JwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtain header that contains jwt
        String authHeader = request.getHeader("Authorization"); //Bearer Jwt
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request , response);
            return;
        }
        // Obtain jwt token
        String token = authHeader.split(" ")[1];
        // Obtain subject/username in Jwt
        String username = JwtService.extractUsername(token);
        // Set Authenticate object inside our security context
        User user = userRepository.findByUsername(username).get();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
        //execute rest of filters
        filterChain.doFilter(request, response);


    }
}
