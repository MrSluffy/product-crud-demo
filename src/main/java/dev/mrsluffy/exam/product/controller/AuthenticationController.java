package dev.mrsluffy.exam.product.controller;

import dev.mrsluffy.exam.auth.model.AuthenticationRequest;
import dev.mrsluffy.exam.auth.model.RegisterRequest;
import dev.mrsluffy.exam.product.service.AuthenticationService;
import dev.mrsluffy.exam.util.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/12/2024
 **/

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (HttpClientErrorException.BadRequest err) {
            return ResponseEntity.badRequest().body(Utilities.error(err.getMessage()));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(Utilities.errorTraceId(exception.getMessage()));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (Exception err) {
            return ResponseEntity.badRequest().body(Utilities.error(err.getMessage()));
        }
    }

}
