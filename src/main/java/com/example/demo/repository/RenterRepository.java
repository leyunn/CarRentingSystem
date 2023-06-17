package com.example.demo.repository;
import com.example.demo.model.Renter;
import com.example.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RenterRepository extends JpaRepository<Renter, Integer> {
    @Query("SELECT r FROM Renter r WHERE r.userId = ?1")
    Renter findByUserId(Long userId);
}
