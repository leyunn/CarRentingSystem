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

    @GetMapping("/cars")
    public ResponseEntity<?> getCars(@RequestParam CarStatus carStatus) {
//        if(!accountService.isAuthenticated(id, token)){
//            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
//        }
        return ResponseEntity.status(200).body(carService.getAllCars(carStatus));
    }

    @GetMapping("/noPowerCars")
    public ResponseEntity<?> getCars() {
//        if(!accountService.isAuthenticated(id, token)){
//            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
//        }
        return ResponseEntity.status(200).body(carService.getNoPowerCars());
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> fixCar(@RequestBody FixForm fixForm, @PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if(user.getIsRenter()){
            return ResponseEntity.status(403).body(new ErrorResponse("not a repairer"));
        }
        Car car = carService.findCar(fixForm.getCarNo());
        if(car==null){
            return ResponseEntity.status(404).body(new ErrorResponse("car not found"));
        }
        carService.changeStatus(car, fixForm.getStatus());
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    @PostMapping("/charge/{id}")
    public ResponseEntity<?> chargeCar(@RequestBody ChargeForm chargeForm, @PathVariable long id, @RequestHeader("Authorization") String token) {
        if(!accountService.isAuthenticated(id, token)){
            return ResponseEntity.status(401).body(new ErrorResponse("authentication failed"));
        }
        UserInfo user = accountService.findUserInfo(id);
        if(user.getIsRenter()){
            return ResponseEntity.status(403).body(new ErrorResponse("not a repairer"));
        }
        Car car = carService.findCar(chargeForm.getCarNo());
        if(car==null){
            return ResponseEntity.status(404).body(new ErrorResponse("car not found"));
        }
        carService.charge(car);
        return ResponseEntity.status(200).body(util.EMPTY_RESPONSE);
    }

    @Data
    @NoArgsConstructor
    public static class FixForm{
        private String carNo;
        private CarStatus status;

    }

    @Data
    @NoArgsConstructor
    public static class ChargeForm{
        private String carNo;
    }


}
