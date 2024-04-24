package com.lorem.JavaSecurityJWT.authentication;

import com.lorem.JavaSecurityJWT.user.User;
import com.lorem.JavaSecurityJWT.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lorem.JavaSecurityJWT.config.jwtService;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private jwtService JwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(User request){
        var user = request;
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        String token = JwtService.generateToken(user , generateExtraClaims(user));
        return new AuthenticationResponse(token);
    }


    public AuthenticationResponse login(AuthenticationRequest request){

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        authenticationManager.authenticate(token);
        User user = userRepository.findByUsername(request.getUsername()).get();

        String jwt = JwtService.generateToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(jwt);

    }

    private Map<String , Object> generateExtraClaims(User user) {
        Map<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("role",user.getRole().name());

        return extraClaims;
    }

}
