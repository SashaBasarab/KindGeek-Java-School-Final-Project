package basarab.olexandr.springfinalproject.controller;

import basarab.olexandr.springfinalproject.dto.request.LoginRequest;
import basarab.olexandr.springfinalproject.dto.request.SignupRequest;
import basarab.olexandr.springfinalproject.dto.response.JwtResponse;
import basarab.olexandr.springfinalproject.dto.response.MessageResponse;
import basarab.olexandr.springfinalproject.exceptions.UserAlreadyExists;
import basarab.olexandr.springfinalproject.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        log.info("request: {}", loginRequest);
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signUpRequest) {
        log.info("request: {}", signUpRequest);
        try {
            authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (UserAlreadyExists e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: username or email is already taken!"));
        }
    }

}
