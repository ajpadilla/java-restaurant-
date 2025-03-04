package com.restaurant.gateway.auth;

/*import com.restaurant.gateway.config.JwtService;
import com.restaurant.gateway.user.User;
import com.restaurant.gateway.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                //.role(Role.USER)
                .role(request.getRole())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        //var jwtToken = jwtService.generateToken(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        // Generar el token con las reclamaciones adicionales
        String jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}*/
