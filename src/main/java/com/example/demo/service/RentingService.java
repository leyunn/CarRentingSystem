package com.example.demo.service;

import com.example.demo.controller.AccountController;
import com.example.demo.model.*;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ChargeStationRepository;
import com.example.demo.repository.RentRecordRepository;
import com.example.demo.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.service.distance;
@Service
public class RentingService {
    @Autowired
    private RenterRepository renterRepo;

    @Autowired
    private CarRepository carRepo;
    @Autowired
    private RentRecordRepository rentRecordRepo;

    public Renter findRenter(Long userId){
        return renterRepo.findByUserId(userId);
    }

    public List<RentRecord> getRentRecords(Long userId){
        return rentRecordRepo.findById(userId);
    }
    public void createRenter(Long userId){
        Renter user = new Renter();
        user.setUserId(userId);
        renterRepo.save(user);
    }

    public void rent(Car car, Renter user){
        user.setCurrentCar(car);
        RentRecord current_renting_record = new RentRecord();
        current_renting_record.setStartTime(LocalDateTime.now());
        current_renting_record.setStart_lng(car.getLng());
        current_renting_record.setStart_lat(car.getLat());
        current_renting_record.setUserid(user.getUserId());
        user.setCurrentRenting(current_renting_record);
        car.setRiding(true);
        user.setRenting(true);
        rentRecordRepo.save(current_renting_record);
        renterRepo.save(user);
    }

    public int getBill(double distance, boolean usedCoupon){
        int bill = (int)distance*3;
        if(usedCoupon){
            bill = (int)(bill*0.9);
        }
        return  bill;
    }

    public void returnAndPay(Renter user, double distance, double lat, double lng, boolean usedCoupon, int power, int chargeCount){
        //end rent
        RentRecord current_renting_record = user.getCurrentRenting();

        current_renting_record.setEndTime(LocalDateTime.now());
        current_renting_record.setEnd_lat(lat);
        current_renting_record.setEnd_lng(lng);
        current_renting_record.setDistance(distance);
        Duration duration = Duration.between(current_renting_record.getStartTime(), current_renting_record.getEndTime());
        current_renting_record.setTotalMinutes(duration.toMinutes());
        current_renting_record.setBill(getBill(distance, usedCoupon));
        current_renting_record.setChargeCount(chargeCount);
        //return
        rentRecordRepo.save(current_renting_record);
        user.setCurrentRenting(null);
        user.setRenting(false);
        Car car = user.getCurrentCar();
        car.setLat(lat);
        car.setLng(lng);
        car.setPower(power);
        car.setRiding(false);
        user.setCurrentCar(null);
        renterRepo.save(user);
        carRepo.save(car);

    }


}
