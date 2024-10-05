package com.test_auth.aws.controllers;

import com.test_auth.aws.entities.User;
import com.test_auth.aws.services.IUserService;
import com.test_auth.aws.shared.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping("/listUsers")
    public ResponseEntity<BaseResponse> GetUsers(){
        try {
            List<User> users = userService.GetUsers();
            return new ResponseEntity<>(BaseResponse.success(users), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<BaseResponse> DeleteUser(@PathVariable Long id){
        try {
            userService.DeleteUser(id);
            return new ResponseEntity<>(BaseResponse.success(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(BaseResponse.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
