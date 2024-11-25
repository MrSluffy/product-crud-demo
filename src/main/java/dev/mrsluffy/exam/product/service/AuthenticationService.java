package dev.mrsluffy.exam.product.service;

import dev.mrsluffy.exam.auth.model.AuthenticationRequest;
import dev.mrsluffy.exam.auth.model.RegisterRequest;
import dev.mrsluffy.exam.product.data.entities.user.User;
import dev.mrsluffy.exam.product.data.entities.user.UserRole;
import dev.mrsluffy.exam.product.repository.UserRepository;
import dev.mrsluffy.exam.util.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/12/2024
 **/

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        } else {
            var user =
                    User.builder()
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(UserRole.GENERAL)
                            .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return Utilities.message("token", jwtToken);
        }
    }

    public Map<String, String> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return Utilities.message("token", jwtToken);
    }
}