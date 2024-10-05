package com.test_auth.aws.services;

import com.test_auth.aws.entities.User;
import com.test_auth.aws.request.AuthRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> GetUsers();

    Optional<User> GetUserById(Long id);
    void SaveUser(User user);
    void DeleteUser(Long id);

    void UpdateUser(User user);

    ResponseEntity<?> Login(AuthRequest authRequest);
}
