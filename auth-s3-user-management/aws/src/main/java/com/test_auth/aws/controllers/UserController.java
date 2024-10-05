package com.test_auth.aws.controllers;

import com.test_auth.aws.entities.User;
import com.test_auth.aws.services.IUserService;
import com.test_auth.aws.services.S3Service;
import com.test_auth.aws.shared.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public IUserService userService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> SaveUser(@ModelAttribute User user, MultipartFile file){
        try {
            if (file != null){
                ResponseEntity<String> response = s3Service.uploadFile(file);
                if(response.getStatusCode().equals(HttpStatus.OK)){
                    user.setAvatarUrl(response.getBody());
                }
                else {
                   return new ResponseEntity<>(BaseResponse.error(response.getBody()),
                           response.getStatusCode());
                }
            }
            userService.SaveUser(user);
            return new ResponseEntity<>(BaseResponse.success(), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>( BaseResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<?> EditUser(@RequestBody User user){
        try {
            Optional<User> searchedUser = userService.GetUserById(user.getId());
            if(searchedUser.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userService.UpdateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
