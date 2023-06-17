package com.example.demo.repository;
import com.example.demo.model.Car;
import com.example.demo.model.ChargeStation;
import com.example.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargeStationRepository extends JpaRepository<ChargeStation, Integer> {
    @Query("SELECT c FROM ChargeStation c WHERE " +
            "c.lat >= :minLatitude AND " +
            "c.lat <= :maxLatitude AND " +
            "c.lng >= :minLongitude AND " +
            "c.lng <= :maxLongitude")
    List<ChargeStation> findBySquare(@Param("minLatitude") double minLatitude,
                           @Param("maxLatitude") double maxLatitude,
                           @Param("minLongitude") double minLongitude,
                           @Param("maxLongitude") double maxLongitude);
}
