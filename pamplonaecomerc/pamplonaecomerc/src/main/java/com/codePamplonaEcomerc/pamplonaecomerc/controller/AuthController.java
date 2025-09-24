package com.codePamplonaEcomerc.pamplonaecomerc.controller;

import com.codePamplonaEcomerc.pamplonaecomerc.dto.AuthenticationRequest;
import com.codePamplonaEcomerc.pamplonaecomerc.dto.SignupRequest;
import com.codePamplonaEcomerc.pamplonaecomerc.dto.UserDto;
import com.codePamplonaEcomerc.pamplonaecomerc.entity.User;
import com.codePamplonaEcomerc.pamplonaecomerc.repository.UserRepository;
import com.codePamplonaEcomerc.pamplonaecomerc.services.auth.AuthService;
import com.codePamplonaEcomerc.pamplonaecomerc.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario o contraseña incorrectos");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        Optional<User> optionalUser = userRepository.findFirtsByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            // Crear el objeto JSON con los datos del usuario y el token
            JSONObject jsonResponse = new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .put("token", jwt);

            // Configurar las cabeceras y retornar la respuesta
            return ResponseEntity.ok()
                    .header("Access-Control-Expose-Headers", "Authorization")
                    .header("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header")
                    .header(HEADER_STRING, TOKEN_PREFIX + jwt)
                    .body(jsonResponse.toString());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("Usuario ya existe", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}


