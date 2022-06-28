package basarab.olexandr.springfinalproject.service;

import basarab.olexandr.springfinalproject.dto.request.LoginRequest;
import basarab.olexandr.springfinalproject.dto.request.SignupRequest;
import basarab.olexandr.springfinalproject.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public JwtResponse authenticateUser(LoginRequest loginRequest);

    public void registerUser(SignupRequest signUpRequest);

}
