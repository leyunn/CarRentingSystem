package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.ChargeStation;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ChargeStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/load")
public class LoadingController {
    @Autowired
    private CarRepository carRepo;
    @Autowired
    private ChargeStationRepository chargeStationRepo;

    @PostMapping("/car")
    public ResponseEntity<String> loadCars(@RequestBody List<Car> cars) {
        carRepo.saveAll(cars);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cars loaded successfully");
    }

    @PostMapping("/chargeStation")
    public ResponseEntity<String> loadBattery(@RequestBody List<ChargeStation> batteries) {
        chargeStationRepo.saveAll(batteries);
        return ResponseEntity.status(HttpStatus.CREATED).body("Batteries loaded successfully");
    }

}
