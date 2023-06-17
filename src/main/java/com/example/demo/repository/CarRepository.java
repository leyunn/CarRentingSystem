package com.example.demo.repository;
import com.example.demo.model.Car;
import com.example.demo.model.CarStatus;
import com.example.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT c FROM Car c WHERE " +
            "c.lat >= :minLatitude AND " +
            "c.lat <= :maxLatitude AND " +
            "c.lng >= :minLongitude AND " +
            "c.lng <= :maxLongitude AND " +
            "c.isRiding = false AND " +
            "c.carStatus = NORMAL")
    List<Car> findNormalBySquare(@Param("minLatitude") double minLatitude,
                           @Param("maxLatitude") double maxLatitude,
                           @Param("minLongitude") double minLongitude,
                           @Param("maxLongitude") double maxLongitude);

    @Query("SELECT c FROM Car c WHERE c.carStatus = ?1")
    List<Car> findByStatus(CarStatus carStatus);
    @Query("SELECT c FROM Car c WHERE c.no = ?1")
    Car findByNo(String no);
    @Query("SELECT c FROM Car c WHERE c.power <= ?1")
    List<Car> findByPower(int power);

}
