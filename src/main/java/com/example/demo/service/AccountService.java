/**
 * The AccountService class provides functionality related to user accounts and authentication.
 * It interacts with the UserInfoRepository to perform CRUD operations on user information.
 */
package com.example.demo.service;

import com.example.demo.controller.AccountController;
import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {
    private final Map<Long, String> userMap = new HashMap<>();

    @Autowired
    private UserInfoRepository userInfoRepo;

    /**
     * Creates a new user based on the provided signup form data.
     *
     * @param signupForm The signup form containing user details.
     * @return The created UserInfo object.
     */
    public UserInfo createUser(AccountController.SignupForm signupForm) {
        UserInfo user = new UserInfo();
        user.setUsername(signupForm.getUsername());
        user.setPassword(signupForm.getPassword());
        user.setCardNumber(signupForm.getCardNumber());
        user.setSafeNumber(signupForm.getSafeNumber());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setIsRenter(true);
        userInfoRepo.save(user);
        return user;
    }

    /**
     * Creates a new repairer based on the provided login form data.
     *
     * @param loginForm The login form containing user details.
     * @return The created UserInfo object.
     */
    public UserInfo createRepairer(AccountController.LoginForm loginForm) {
        UserInfo user = new UserInfo();
        user.setUsername(loginForm.getUsername());
        user.setPassword(loginForm.getPassword());
        user.setIsRenter(false);
        userInfoRepo.save(user);
        return user;
    }

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean checkUserExist(String username) {
        return userInfoRepo.existsByUsername(username);
    }

    /**
     * Finds and returns the UserInfo object for the given username.
     *
     * @param username The username to search for.
     * @return The found UserInfo object, or null if not found.
     */
    public UserInfo findUserInfo(String username) {
        return userInfoRepo.findByUsername(username);
    }

    /**
     * Finds and returns the UserInfo object for the given user ID.
     *
     * @param id The user ID to search for.
     * @return The found UserInfo object, or null if not found.
     */
    public UserInfo findUserInfo(Long id) {
        return userInfoRepo.findById(id);
    }

    /**
     * Verifies if the provided password matches the user's password.
     *
     * @param user     The UserInfo object representing the user.
     * @param password The password to verify.
     * @return true if the password is valid, false otherwise.
     */
    public boolean verifyUserInfo(UserInfo user, String password) {
        // TODO: Add encoder for password comparison
        return user.getPassword().equals(password);
    }

    /**
     * Adds the specified number of coupons to the user's account.
     *
     * @param user  The UserInfo object representing the user.
     * @param count The number of coupons to add.
     */
    public void addCoupon(UserInfo user, int count) {
        user.setCouponCount(user.getCouponCount() + count);
    }

    /**
     * Decreases the user's coupon count by one.
     *
     * @param user The UserInfo object representing the user.
     */
    public void useCoupon(UserInfo user) {
        user.setCouponCount(user.getCouponCount() - 1);
    }

    /**
     * Checks if the provided user ID and token match the stored values for authentication.
     *
     * @param userID The user ID.
     * @param token  The authentication token.
     * @return true if the authentication is successful, false otherwise.
     */
    public boolean isAuthenticated(Long userID, String token) {
        // Check if the provided user ID and token match the stored values
        String storedToken = userMap.get(userID);
        return storedToken != null && storedToken.equals(token);
    }

    /**
     * Generates a new authentication token for the given user ID and adds it to the userMap.
     *
     * @param userID The user ID.
     * @return The generated authentication token.
     */
    public String login(Long userID) {
        String token = userMap.get(userID);
        if (token == null) {
            token = UUID.randomUUID().toString();
            userMap.put(userID, token);
        }
        return token;
    }

    /**
     * Removes the authentication token associated with the given user ID from the userMap.
     *
     * @param userID The user ID.
     * @return true if the logout is successful, false otherwise.
     */
    public boolean logout(Long userID) {
        String token = userMap.get(userID);
        if (token == null) {
            return false;
        }
        userMap.remove(userID);
        return true;
    }
}
