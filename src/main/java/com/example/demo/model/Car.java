package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    private String no;
    private double lat;
    private double lng;
    private int power;
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    private boolean isRiding;

    public Car(String no, int power, double lat, double lng) {
        this.no = no;
        this.lat = lat;
        this.lng = lng;
        this.power = power;
        this.carStatus = CarStatus.NORMAL;
        this.isRiding = false;

    }
}
