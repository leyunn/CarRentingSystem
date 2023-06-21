/**
 * The AccountController class is responsible for handling account-related requests.
 * It provides endpoints for user signup, login, logout, and retrieving user information.
 */
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

    /**
     * Retrieves the information of a user.
     *
     * @param id    The ID of the user.
     * @param token The authentication token.
     * @return ResponseEntity representing the user information or an error response.
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getInfo(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if (user == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.status(200).body(user);
    }

    /**
     * Creates a new repairer account.
     *
     * @param loginForm The login form containing the necessary information.
     * @return ResponseEntity representing the success or failure of the signup process.
     */
    @PostMapping("/newRepairer")
    public ResponseEntity<?> signup(@RequestBody LoginForm loginForm) {
        if (!loginForm.isValid()) {
            return ResponseEntity.status(415).body(new ErrorResponse("Format error"));
        }
        if (accountService.checkUserExist(loginForm.getUsername())) {
            return ResponseEntity.status(409).body(new ErrorResponse("User already exists"));
        }
        UserInfo user = accountService.createRepairer(loginForm);
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    /**
     * Creates a new user account.
     *
     * @param signupForm The signup form containing the necessary information.
     * @return ResponseEntity representing the success or failure of the signup process.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupForm signupForm) {
        if (!signupForm.isValid()) {
            return ResponseEntity.status(415).body(new ErrorResponse("Format error"));
        }
        if (accountService.checkUserExist(signupForm.getUsername())) {
            return ResponseEntity.status(409).body(new ErrorResponse("User already exists"));
        }
        UserInfo user = accountService.createUser(signupForm);
        rentingService.createRenter(user.getId());
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    /**
     * Performs user login.
     *
     * @param loginForm The login form containing the username and password.
     * @param request   The HttpServletRequest object.
     * @return ResponseEntity representing the success or failure of the login process.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        UserInfo user = accountService.findUserInfo(loginForm.getUsername());
        if (user == null || !accountService.verifyUserInfo(user, loginForm.getPassword())) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        String token = accountService.login(user.getId());
        return ResponseEntity.status(200).body(new UserTokenResponse(user.getId(), token, user.getIsRenter()));
    }

    /**
     * Performs user logout.
     *
     * @param id    The ID of the user.
     * @param token The authentication token.
     * @return ResponseEntity representing the success or failure of the logout process.
     */
    @PostMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }

        if (accountService.logout(id)) {
            return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
        } else {
            return ResponseEntity.status(403).body(new ErrorResponse("Not logged in"));
        }
    }

    /**
     * SignupForm is a nested class used for representing the signup form data.
     */
    @Data
    @NoArgsConstructor
    public static class SignupForm {
        private String username;
        private String password;
        private String cardNumber;
        private String safeNumber;
        private String phoneNumber;
        private String email;

        /**
         * Checks if the signup form data is valid.
         *
         * @return true if the form data is valid, false otherwise.
         */
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

    /**
     * LoginForm is a nested class used for representing the login form data.
     */
    @Data
    @NoArgsConstructor
    public static class LoginForm {
        private String username;
        private String password;

        /**
         * Checks if the login form data is valid.
         *
         * @return true if the form data is valid, false otherwise.
         */
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
