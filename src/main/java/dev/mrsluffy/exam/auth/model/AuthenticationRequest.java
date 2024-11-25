package dev.mrsluffy.exam.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/12/2024
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String email;
    String password;
}
