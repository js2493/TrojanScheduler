package com.trojanscheduler.project.repository;

import com.trojanscheduler.project.model.TrojanUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrojanUserRepository extends JpaRepository<TrojanUser, Long> {

    @Query("SELECT u FROM users u WHERE u.username = :username")
    TrojanUser findByUsername(@Param("username") String username);

}
