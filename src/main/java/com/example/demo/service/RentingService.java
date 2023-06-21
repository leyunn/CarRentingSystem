/**
 * The RentingService class provides functionality related to renting and returning cars.
 * It interacts with the RenterRepository, CarRepository, and RentRecordRepository to perform database operations.
 */
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

    /**
     * Finds and returns the renter with the specified user ID.
     *
     * @param userId The user ID.
     * @return The found renter, or null if not found.
     */
    public Renter findRenter(Long userId) {
        return renterRepo.findByUserId(userId);
    }

    /**
     * Retrieves a list of rent records for the specified user ID.
     *
     * @param userId The user ID.
     * @return The list of rent records for the specified user ID.
     */
    public List<RentRecord> getRentRecords(Long userId) {
        return rentRecordRepo.findById(userId);
    }

    /**
     * Creates a new renter with the specified user ID.
     *
     * @param userId The user ID.
     */
    public void createRenter(Long userId) {
        Renter user = new Renter();
        user.setUserId(userId);
        renterRepo.save(user);
    }

    /**
     * Rent a car for the specified user.
     *
     * @param car  The car to rent.
     * @param user The renter.
     */
    public void rent(Car car, Renter user) {
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

    /**
     * Calculates the bill based on the distance traveled and whether a coupon was used.
     *
     * @param distance   The distance traveled.
     * @param usedCoupon Whether a coupon was used.
     * @return The calculated bill amount.
     */
    public int getBill(double distance, boolean usedCoupon) {
        int bill = (int) distance * 3;
        if (usedCoupon) {
            bill = (int) (bill * 0.9);
        }
        return bill;
    }

    /**
     * Returns the rented car and completes the payment for the rent.
     *
     * @param user        The renter.
     * @param distance    The distance traveled.
     * @param lat         The latitude of the return location.
     * @param lng         The longitude of the return location.
     * @param usedCoupon  Whether a coupon was used.
     * @param power       The power level of the car.
     * @param chargeCount The number of times the car was charged.
     */
    public void returnAndPay(Renter user, double distance, double lat, double lng, boolean usedCoupon, int power, int chargeCount) {
        // End rent
        RentRecord current_renting_record = user.getCurrentRenting();
        current_renting_record.setEndTime(LocalDateTime.now());
        current_renting_record.setEnd_lat(lat);
        current_renting_record.setEnd_lng(lng);
        current_renting_record.setDistance(distance);
        Duration duration = Duration.between(current_renting_record.getStartTime(), current_renting_record.getEndTime());
        current_renting_record.setTotalMinutes(duration.toMinutes());
        current_renting_record.setBill(getBill(distance, usedCoupon));
        current_renting_record.setChargeCount(chargeCount);
        // Return
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
