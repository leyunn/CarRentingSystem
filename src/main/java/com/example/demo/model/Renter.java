package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Renter {
    @Id
    private Long userId;
    @OneToOne
    private RentRecord currentRenting = null;
//    private List<RentRecord> rentRecords;
    private boolean isRenting = false;
    @OneToOne
    private Car currentCar = null;

}
