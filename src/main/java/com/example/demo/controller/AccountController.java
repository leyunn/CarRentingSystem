package com.example.demo.controller;
import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.service.AccountService;
import com.example.demo.response.*;
import com.example.demo.service.RentingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RentingService rentingService;

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getInfo(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if(user==null){
            return ResponseEntity.status(401).body(new ErrorResponse("user not found"));
        }
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping("/newRepairer")
    public ResponseEntity<?> signup(@RequestBody LoginForm loginForm) {
        if(!loginForm.isValid()){
            return ResponseEntity.status(415).body(new ErrorResponse("format error"));
        }
        if(accountService.checkUserExist(loginForm.getUsername())){
            return ResponseEntity.status(409).body(new ErrorResponse("user exist"));
        }
        UserInfo user = accountService.createRepairer(loginForm);
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupForm signupForm) {
        if(!signupForm.isValid()){
            return ResponseEntity.status(415).body(new ErrorResponse("format error"));
        }
        if(accountService.checkUserExist(signupForm.getUsername())){
            return ResponseEntity.status(409).body(new ErrorResponse("user exist"));
        }
        UserInfo user = accountService.createUser(signupForm);
        rentingService.createRenter(user.getId());
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        UserInfo user = accountService.findUserInfo(loginForm.getUsername());
        if(user==null || !accountService.verifyUserInfo(user, loginForm.getPassword())){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        String token = accountService.login(user.getId());
        return ResponseEntity.status(200).body(new UserTokenResponse(user.getId(), token, user.getIsRenter()));
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }

        if(accountService.logout(id)){
            return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
        }else{
            return ResponseEntity.status(403).body(new ErrorResponse("not logged in"));
        }

    }

    @Data
    @NoArgsConstructor
    public static class SignupForm{
        private String username;
        private String password;
        private String cardNumber;
        private String safeNumber;
        private String phoneNumber;
        private String email;

        public boolean isValid() {
            if (username == null || username.length() == 0) {
                return false;
            }
            if (cardNumber == null || cardNumber.length() != 16) {
                return false;
            }
            if (safeNumber == null || safeNumber.length() != 3) {
                return false;
            }
            if (phoneNumber == null || phoneNumber.length() < 9) {
                return false;
            }
            if (password == null || password.length() < 6) {
                return false;
            }
            if (email == null || !email.matches(".+@.+\\..+")) {
                return false;
            }
            return true;
        }

    }

    @Data
    @NoArgsConstructor
    public static class LoginForm{
        private String username;
        private String password;

        public boolean isValid() {
            if (username == null || username.length() == 0) {
                return false;
            }
            if (password == null || password.length() < 6) {
                return false;
            }
            return true;
        }
    }
}
