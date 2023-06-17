package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.Renter;
import com.example.demo.model.UserInfo;
import com.example.demo.response.ErrorResponse;
import com.example.demo.response.util;
import com.example.demo.service.AccountService;
import com.example.demo.service.CarService;
import com.example.demo.service.RentingService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/renting")
public class RentingController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RentingService rentingService;

    @Autowired
    private CarService carService;
    @GetMapping("/status/{id}")
    public ResponseEntity<?> getStatus(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        return ResponseEntity.status(200).body(rentingService.findRenter(accountService.findUserInfo(id).getId()));

    }

    @GetMapping("/records/{id}")
    public ResponseEntity<?> getRecords(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        return ResponseEntity.status(200).body(rentingService.getRentRecords(accountService.findUserInfo(id).getId()));

    }

    @GetMapping("/cars")
    public ResponseEntity<?> getCars(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double range) {
//        if(!accountService.isAuthenticated(id, token)){
//            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
//        }
        return ResponseEntity.status(200).body(carService.getNormalCars(latitude, longitude, range));

    }

    @GetMapping("/chargeStation")
    public ResponseEntity<?> getChargeStations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double range) {
//        if(!accountService.isAuthenticated(id, token)){
//            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
//        }
        return ResponseEntity.status(200).body(carService.getBatteries(latitude, longitude, range));

    }

    @PostMapping("/rent/{id}")
    public ResponseEntity<?> rent(@RequestBody RentForm rentForm, @PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        Renter user = rentingService.findRenter(id);
        if(user.isRenting()){
            return ResponseEntity.status(403).body(new ErrorResponse("already renting"));
        }
        Car car = carService.findCar(rentForm.getCarNo());
        if(car==null){
            return ResponseEntity.status(404).body(new ErrorResponse("car not found"));
        }
        if(car.isRiding()){
            return ResponseEntity.status(403).body(new ErrorResponse("car in used"));
        }
        rentingService.rent(car, user);
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);

    }

    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnAndPay(@PathVariable long id, @RequestHeader("Authorization") String token, @RequestBody ReturnForm returnForm) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        Renter renter = rentingService.findRenter(id);
        if(!renter.isRenting()){
            return ResponseEntity.status(403).body(new ErrorResponse("not renting"));
        }
        if(!returnForm.inRange()){
            return ResponseEntity.status(403).body(new ErrorResponse("not in range"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if(returnForm.isUsedCoupon()){
            if(user.getCouponCount() <1){
                return ResponseEntity.status(403).body(new ErrorResponse("not enough coupon"));
            }
            accountService.useCoupon(user);
        }
        accountService.addCoupon(user, returnForm.getChargeCount());
        rentingService.returnAndPay(renter, returnForm.getDistance(), returnForm.getLat(), returnForm.getLng(), returnForm.isUsedCoupon(), returnForm.getPower(), returnForm.getChargeCount());
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);

    }

    @Data
    @NoArgsConstructor
    public static class RentForm{
        private String carNo;

    }

    @Data
    @NoArgsConstructor
    public static class ReturnForm{
        private double lat;
        private double lng;

        private double distance;

        private int power;

        private int chargeCount;
        private boolean isUsedCoupon;

        public boolean inRange(){
            if(lng < 121.511162 || lng >121.567045) return  false;
            if(lat < 25.026708 || lat > 25.068277) return false;
            return  true;
        }

    }


}
