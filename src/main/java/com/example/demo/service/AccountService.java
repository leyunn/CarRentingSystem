package com.example.demo.service;
import com.example.demo.controller.AccountController;
import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {
    private final Map<Long, String> userMap = new HashMap<>();
    @Autowired
    private UserInfoRepository userInfoRepo;
    public UserInfo createUser(AccountController.SignupForm signupForm){
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

    public UserInfo createRepairer(AccountController.LoginForm loginForm){
        UserInfo user = new UserInfo();
        user.setUsername(loginForm.getUsername());
        user.setPassword(loginForm.getPassword());
        user.setIsRenter(false);
        userInfoRepo.save(user);
        return user;
    }

    public boolean checkUserExist(String username){
        return userInfoRepo.existsByUsername(username);
    }

    public UserInfo findUserInfo(String username){
        return userInfoRepo.findByUsername(username);
    }

    public UserInfo findUserInfo(Long id){
        return userInfoRepo.findById(id);
    }

    public boolean verifyUserInfo(UserInfo user, String password){
        //TODO: add encoder
        return user.getPassword().equals(password);
    }

    public void addCoupon(UserInfo user, int count){
        user.setCouponCount(user.getCouponCount()+count);
    }

    public void useCoupon(UserInfo user){
        user.setCouponCount(user.getCouponCount()-1);
    }

    public boolean isAuthenticated(Long userID, String token) {
        // Check if the provided user number and token match the stored values
        String storedToken = userMap.get(userID);
        return storedToken != null && storedToken.equals(token);
    }

    public String login(Long userID) {
        String token = userMap.get(userID);
        if(token == null){
            token = UUID.randomUUID().toString();
            userMap.put(userID, token);
        }
        return token;
    }

    public boolean logout(Long userID) {
        String token = userMap.get(userID);
        if(token == null){
            return false;
        }
        userMap.remove(userID);
        return true;
    }



}
