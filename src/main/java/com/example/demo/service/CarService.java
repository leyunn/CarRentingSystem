package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ChargeStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepo;
    @Autowired
    private ChargeStationRepository chargeStationRepo;
    public List<Car> getNormalCars(double latitude, double longitude, double range){
        double minLatitude = latitude - range;
        double maxLatitude = latitude + range;
        double minLongitude = longitude - range;
        double maxLongitude = longitude + range;
        return carRepo.findNormalBySquare(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    public List<Car> getAllCars(CarStatus carStatus){
        return carRepo.findByStatus(carStatus);
    }

    public List<Car> getNoPowerCars(){
        return carRepo.findByPower(20);
    }

    public List<ChargeStation> getBatteries(double latitude, double longitude, double range){
        double minLatitude = latitude - range;
        double maxLatitude = latitude + range;
        double minLongitude = longitude - range;
        double maxLongitude = longitude + range;
        return chargeStationRepo.findBySquare(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    public Car findCar(String no){
        Car car = carRepo.findByNo(no);
        return car;
    }

    public void changeStatus(Car car, CarStatus status){
        car.setCarStatus(status);
        carRepo.save(car);
    }


    public void charge(Car car){
        car.setPower(100);
        carRepo.save(car);
    }


}
