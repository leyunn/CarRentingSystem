/**
 * The CarService class provides functionality related to cars and charge stations.
 * It interacts with the CarRepository and ChargeStationRepository to perform database operations.
 */
package com.example.demo.service;

import com.example.demo.model.Car;
import com.example.demo.model.CarStatus;
import com.example.demo.model.ChargeStation;
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

    /**
     * Retrieves a list of normal cars within the specified range of latitude and longitude.
     *
     * @param latitude  The latitude.
     * @param longitude The longitude.
     * @param range     The range in which to search for normal cars.
     * @return The list of normal cars within the specified range.
     */
    public List<Car> getNormalCars(double latitude, double longitude, double range) {
        double minLatitude = latitude - range;
        double maxLatitude = latitude + range;
        double minLongitude = longitude - range;
        double maxLongitude = longitude + range;
        return carRepo.findNormalBySquare(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    /**
     * Retrieves a list of cars with the specified car status.
     *
     * @param carStatus The car status.
     * @return The list of cars with the specified car status.
     */
    public List<Car> getAllCars(CarStatus carStatus) {
        return carRepo.findByStatus(carStatus);
    }

    /**
     * Retrieves a list of cars with no power.
     *
     * @return The list of cars with no power.
     */
    public List<Car> getNoPowerCars() {
        return carRepo.findByPower(20);
    }

    /**
     * Retrieves a list of charge stations within the specified range of latitude and longitude.
     *
     * @param latitude  The latitude.
     * @param longitude The longitude.
     * @param range     The range in which to search for charge stations.
     * @return The list of charge stations within the specified range.
     */
    public List<ChargeStation> getBatteries(double latitude, double longitude, double range) {
        double minLatitude = latitude - range;
        double maxLatitude = latitude + range;
        double minLongitude = longitude - range;
        double maxLongitude = longitude + range;
        return chargeStationRepo.findBySquare(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    /**
     * Finds and returns the car with the specified car number.
     *
     * @param no The car number.
     * @return The found car, or null if not found.
     */
    public Car findCar(String no) {
        Car car = carRepo.findByNo(no);
        return car;
    }

    /**
     * Changes the status of the specified car.
     *
     * @param car    The car to update.
     * @param status The new car status.
     */
    public void changeStatus(Car car, CarStatus status) {
        car.setCarStatus(status);
        carRepo.save(car);
    }

    /**
     * Charges the specified car by setting its power to 100% and moving it to a random spot.
     *
     * @param car The car to charge.
     */
    public void charge(Car car) {
        car.setPower(100);
        // Move to random spot
        try {
            Double[] newLocation = distance.generate(car.getLat(), car.getLng());
            car.setLat(newLocation[0]);
            car.setLng(newLocation[1]);
        } catch (Exception e) {
            // Ignore
        }
        carRepo.save(car);
    }
}
