package com.test_auth.aws.controllers;

import com.test_auth.aws.request.AuthRequest;
import com.test_auth.aws.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody AuthRequest authRequest){
        return userService.Login(authRequest);
    }


}
