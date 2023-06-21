/**
 * The RepairingController class is responsible for handling requests related to car repairs and maintenance.
 * It provides endpoints for retrieving cars with specific statuses, retrieving cars with no power,
 * fixing a car, and charging a car.
 */
package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.CarStatus;
import com.example.demo.model.UserInfo;
import com.example.demo.response.util;
import com.example.demo.service.AccountService;
import com.example.demo.service.CarService;
import com.example.demo.response.ErrorResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repairing")
public class RepairingController {

    @Autowired
    private CarService carService;

    @Autowired
    private AccountService accountService;

    /**
     * Retrieves the cars with the specified status.
     *
     * @param carStatus The status of the cars to retrieve.
     * @return ResponseEntity containing the list of cars with the specified status.
     */
    @GetMapping("/cars")
    public ResponseEntity<?> getCars(@RequestParam CarStatus carStatus) {
        return ResponseEntity.status(200).body(carService.getAllCars(carStatus));
    }

    /**
     * Retrieves the cars with no power.
     *
     * @return ResponseEntity containing the list of cars with no power.
     */
    @GetMapping("/noPowerCars")
    public ResponseEntity<?> getCars() {
        return ResponseEntity.status(200).body(carService.getNoPowerCars());
    }

    /**
     * Fixes a car and changes its status.
     *
     * @param fixForm The fix form containing the car number and the new status.
     * @param id      The ID of the user.
     * @param token   The authorization token.
     * @return ResponseEntity representing the success or failure of the fix process.
     */
    @PostMapping("/status/{id}")
    public ResponseEntity<?> fixCar(@RequestBody FixForm fixForm, @PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if (user.getIsRenter()) {
            return ResponseEntity.status(403).body(new ErrorResponse("Not a repairer"));
        }
        Car car = carService.findCar(fixForm.getCarNo());
        if (car == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Car not found"));
        }
        carService.changeStatus(car, fixForm.getStatus());
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    /**
     * Charges a car.
     *
     * @param chargeForm The charge form containing the car number.
     * @param id         The ID of the user.
     * @param token      The authorization token.
     * @return ResponseEntity representing the success or failure of the charging process.
     */
    @PostMapping("/charge/{id}")
    public ResponseEntity<?> chargeCar(@RequestBody ChargeForm chargeForm, @PathVariable long id, @RequestHeader("Authorization") String token) {
        if (!accountService.isAuthenticated(id, token)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication failed"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if (user.getIsRenter()) {
            return ResponseEntity.status(403).body(new ErrorResponse("Not a repairer"));
        }
        Car car = carService.findCar(chargeForm.getCarNo());
        if (car == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Car not found"));
        }
        carService.charge(car);
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    /**
     * The FixForm class represents the fix request form.
     */
    @Data
    @NoArgsConstructor
    public static class FixForm {
        private String carNo;
        private CarStatus status;
    }

    /**
     * The ChargeForm class represents the charge request form.
     */
    @Data
    @NoArgsConstructor
    public static class ChargeForm {
        private String carNo;
    }
}
