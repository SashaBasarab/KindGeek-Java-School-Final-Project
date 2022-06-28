package basarab.olexandr.springfinalproject.service.Impl;

import basarab.olexandr.springfinalproject.dto.request.LoginRequest;
import basarab.olexandr.springfinalproject.dto.request.SignupRequest;
import basarab.olexandr.springfinalproject.dto.response.JwtResponse;
import basarab.olexandr.springfinalproject.dto.response.MessageResponse;
import basarab.olexandr.springfinalproject.entity.Role;
import basarab.olexandr.springfinalproject.entity.User;
import basarab.olexandr.springfinalproject.entity.UserInterest;
import basarab.olexandr.springfinalproject.entity.UserRole;
import basarab.olexandr.springfinalproject.enums.Interests;
import basarab.olexandr.springfinalproject.exceptions.InterestNotFoundException;
import basarab.olexandr.springfinalproject.exceptions.RoleNotFoundException;
import basarab.olexandr.springfinalproject.exceptions.UserAlreadyExists;
import basarab.olexandr.springfinalproject.repository.RoleRepository;
import basarab.olexandr.springfinalproject.repository.UserInterestRepository;
import basarab.olexandr.springfinalproject.repository.UserRepository;
import basarab.olexandr.springfinalproject.security.jwt.JwtUtils;
import basarab.olexandr.springfinalproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserInterestRepository userInterestRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setRoles(roles);
        return jwtResponse;
    }

    @Override
    public void registerUser(SignupRequest signUpRequest) throws UserAlreadyExists{
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExists("User with username: " + signUpRequest.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExists("User with email: " + signUpRequest.getEmail() + " already exists");
        }
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setNationality(signUpRequest.getNationality());
        user.setMotherTongue(signUpRequest.getMotherTongue());
        Set<String> strInterests = signUpRequest.getInterests();
        Set<UserInterest> userInterests = new HashSet<>();
        strInterests.forEach(userInterest -> {
            switch (userInterest) {
                case "anime" -> {
                    UserInterest anime = userInterestRepository.findByInterest(Interests.ANIME)
                            .orElseThrow(() -> new InterestNotFoundException("Interest " + userInterest + " is not found"));
                    userInterests.add(anime);
                }
                case "computer games" -> {
                    UserInterest computerGames = userInterestRepository.findByInterest(Interests.COMPUTER_GAMES)
                            .orElseThrow(() -> new InterestNotFoundException("Interest " + userInterest + " is not found"));
                    userInterests.add(computerGames);
                }
                case "football" -> {
                    UserInterest football = userInterestRepository.findByInterest(Interests.FOOTBALL)
                            .orElseThrow(() -> new InterestNotFoundException("Interest " + userInterest + " is not found"));
                    userInterests.add(football);
                }
                case "basketball" -> {
                    UserInterest basketball = userInterestRepository.findByInterest(Interests.BASKETBALL)
                            .orElseThrow(() -> new InterestNotFoundException("Interest " + userInterest + " is not found"));
                    userInterests.add(basketball);
                }
                case "tennis" -> {
                    UserInterest tennis = userInterestRepository.findByInterest(Interests.TENNIS)
                            .orElseThrow(() -> new InterestNotFoundException("Interest " + userInterest + " is not found"));
                    userInterests.add(tennis);
                }
                default -> {
                    UserInterest nothing = userInterestRepository.findByInterest(Interests.NOTHING)
                            .orElseThrow(() -> new InterestNotFoundException("Interest is not found"));
                    userInterests.add(nothing);
                }
            }
        });
        user.setInterests(userInterests);
        Role userRole = roleRepository.findByName(UserRole.USER_ROLE)
                        .orElseThrow(() -> new RoleNotFoundException("Error: role is not found."));
        user.getRoles().add(userRole);
        userRepository.save(user);
    }

}
