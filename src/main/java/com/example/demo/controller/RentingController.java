/**
 * The RentingController class is responsible for handling requests related to renting cars and managing rental records.
 * It provides endpoints for retrieving rental status, rental records, available cars, available charge stations,
 * renting a car, and returning a car.
 */
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

    /**
     * Retrieves the rental status of a renter.
     *
     * @param id    The ID of the renter.
     * @param token The authorization token.
     * @return ResponseEntity containing the rental status or an error response if authentication fails.
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<?> getStatus(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        return ResponseEntity.status(200).body(rentingService.findRenter(accountService.findUserInfo(id).getId()));
    }

    /**
     * Retrieves the rental records of a renter.
     *
     * @param id    The ID of the renter.
     * @param token The authorization token.
     * @return ResponseEntity containing the rental records or an error response if authentication fails.
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<?> getRecords(@PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        return ResponseEntity.status(200).body(rentingService.getRentRecords(accountService.findUserInfo(id).getId()));
    }

    /**
     * Retrieves the available cars within a specified range of coordinates.
     *
     * @param latitude  The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @param range     The range in which to search for available cars.
     * @return ResponseEntity containing the list of available cars.
     */
    @GetMapping("/cars")
    public ResponseEntity<?> getCars(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double range) {
        return ResponseEntity.status(200).body(carService.getNormalCars(latitude, longitude, range));
    }

    /**
     * Retrieves the available charge stations within a specified range of coordinates.
     *
     * @param latitude  The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @param range     The range in which to search for available charge stations.
     * @return ResponseEntity containing the list of available charge stations.
     */
    @GetMapping("/chargeStation")
    public ResponseEntity<?> getChargeStations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double range) {
        return ResponseEntity.status(200).body(carService.getBatteries(latitude, longitude, range));
    }

    /**
     * Rents a car for a renter.
     *
     * @param rentForm The rent form containing the car number.
     * @param id       The ID of the renter.
     * @param token    The authorization token.
     * @return ResponseEntity representing the success or failure of the rental process.
     */
    @PostMapping("/rent/{id}")
    public ResponseEntity<?> rent(@RequestBody RentForm rentForm, @PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        Renter user = rentingService.findRenter(id);
        if (user.isRenting()) {
            return ResponseEntity.status(403).body(new ErrorResponse("Already renting"));
        }
        Car car = carService.findCar(rentForm.getCarNo());
        if (car == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Car not found"));
        }
        if (car.isRiding()) {
            return ResponseEntity.status(403).body(new ErrorResponse("Car in use"));
        }
        rentingService.rent(car, user);
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    /**
     * Returns a rented car and calculates the payment.
     *
     * @param id         The ID of the renter.
     * @param token      The authorization token.
     * @param returnForm The return form containing return details.
     * @return ResponseEntity representing the success or failure of the return process.
     */
    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnAndPay(@PathVariable long id, @RequestHeader("Authorization") String token, @RequestBody ReturnForm returnForm) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        Renter renter = rentingService.findRenter(id);
        if (!renter.isRenting()) {
            return ResponseEntity.status(403).body(new ErrorResponse("Not renting"));
        }
        if (!returnForm.inRange()) {
            return ResponseEntity.status(403).body(new ErrorResponse("Not in range"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if (returnForm.isUsedCoupon()) {
            if (user.getCouponCount() < 1) {
                return ResponseEntity.status(403).body(new ErrorResponse("Not enough coupons"));
            }
            accountService.useCoupon(user);
        }
        accountService.addCoupon(user, returnForm.getChargeCount());
        rentingService.returnAndPay(renter, returnForm.getDistance(), returnForm.getLat(), returnForm.getLng(), returnForm.isUsedCoupon(), returnForm.getPower(), returnForm.getChargeCount());
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    /**
     * The RentForm class represents the rental request form.
     */
    @Data
    @NoArgsConstructor
    public static class RentForm {
        private String carNo;
    }

    /**
     * The ReturnForm class represents the return request form.
     */
    @Data
    @NoArgsConstructor
    public static class ReturnForm {
        private double lat;
        private double lng;
        private double distance;
        private int power;
        private int chargeCount;
        private boolean isUsedCoupon;

        /**
         * Checks if the return coordinates are within the valid range.
         *
         * @return true if the coordinates are within the valid range, false otherwise.
         */
        public boolean inRange() {
            if (lng < 121.511162 || lng > 121.567045) return false;
            if (lat < 25.026708 || lat > 25.068277) return false;
            return true;
        }
    }
}
