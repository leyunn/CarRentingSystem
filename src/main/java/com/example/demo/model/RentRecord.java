package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;
    private double start_lat;
    private double start_lng;

    private double end_lat;
    private double end_lng;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalMinutes;
    private int ChargeCount = 0;
    private double distance = 0;

    private int bill;

    private boolean usedCoupon;



}
