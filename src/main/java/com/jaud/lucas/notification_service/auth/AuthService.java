package com.jaud.lucas.notification_service.auth;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jaud.lucas.notification_service.auth.entities.AuthResponse;
import com.jaud.lucas.notification_service.auth.entities.Role;
import com.jaud.lucas.notification_service.auth.entities.RoleEnum;
import com.jaud.lucas.notification_service.auth.entities.User;
import com.jaud.lucas.notification_service.auth.entities.UserDTO;
import com.jaud.lucas.notification_service.exceptions.UsernameAlreadyExistsException;

import jakarta.transaction.Transactional;

@Service 
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserService userService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Transactional 
    public User register(UserDTO user) throws UsernameAlreadyExistsException{
        Role role = new Role();
        role.setName(RoleEnum.ROLE_USER);
        User newUser = new User(user.username(), passwordEncoder.encode(user.password()), Set.of(role));

        if(userService.usernameAlreadyExists(user.username())){
            throw new UsernameAlreadyExistsException("username already exists");
        }

        userRepository.save(newUser);

        return newUser;
    }

    public AuthResponse login(UserDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username()).orElseThrow();

        String jwt = jwtService.generateToken(user);

        return new AuthResponse(jwt);
    }

    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }

}
