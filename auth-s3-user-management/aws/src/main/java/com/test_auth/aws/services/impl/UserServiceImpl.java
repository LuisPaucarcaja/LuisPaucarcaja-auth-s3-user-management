package com.test_auth.aws.services.impl;

import com.test_auth.aws.entities.Role;
import com.test_auth.aws.entities.User;
import com.test_auth.aws.repositories.IUserRepository;
import com.test_auth.aws.request.AuthRequest;
import com.test_auth.aws.services.CustomUserDetailsService;
import com.test_auth.aws.services.IUserService;
import com.test_auth.aws.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public List<User> GetUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> GetUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void SaveUser(User user)  {
        try {
            Role role = new Role();
            role.setId(1L);
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    @Override
    public void DeleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void UpdateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (existingUser.getRole() != null && !existingUser.getRole().equals(user.getRole())) {
            throw new UnsupportedOperationException("Role cannot be modified once set.");
        }
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<String> Login(AuthRequest authRequest) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            if(userDetails == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Authentication manager = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                                authRequest.getPassword())
            );
            if(manager.isAuthenticated()){
                String tokenJwt = jwtUtil.generateToken(authRequest.getUsername());
                return new ResponseEntity<>(tokenJwt, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Unauthorized" ,  HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage() ,  HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
