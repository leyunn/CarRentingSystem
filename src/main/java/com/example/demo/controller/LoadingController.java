/**
 * The LoadingController class is responsible for handling requests related to loading data into the system.
 * It provides endpoints for loading cars and charge stations.
 */
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

    /**
     * Loads a list of cars into the system.
     *
     * @param cars The list of cars to be loaded.
     * @return ResponseEntity representing the success or failure of the car loading process.
     */
    @PostMapping("/car")
    public ResponseEntity<String> loadCars(@RequestBody List<Car> cars) {
        carRepo.saveAll(cars);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cars loaded successfully");
    }

    /**
     * Loads a list of charge stations into the system.
     *
     * @param batteries The list of charge stations to be loaded.
     * @return ResponseEntity representing the success or failure of the charge station loading process.
     */
    @PostMapping("/chargeStation")
    public ResponseEntity<String> loadBattery(@RequestBody List<ChargeStation> batteries) {
        chargeStationRepo.saveAll(batteries);
        return ResponseEntity.status(HttpStatus.CREATED).body("Charge stations loaded successfully");
    }
}
