package com.example.demo.repository;
import com.example.demo.model.RentRecord;
import com.example.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentRecordRepository extends JpaRepository<RentRecord, Integer> {
    @Query("SELECT r FROM RentRecord r WHERE r.userid = ?1")
    List<RentRecord> findById(Long userId);
}
