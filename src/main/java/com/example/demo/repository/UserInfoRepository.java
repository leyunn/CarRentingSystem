package com.example.demo.repository;
import com.example.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserInfo u WHERE u.username = ?1")
    boolean existsByUsername(String username);

    @Query("SELECT u FROM UserInfo u WHERE u.username = ?1")
    UserInfo findByUsername(String username);
    UserInfo findById(long id);
}
