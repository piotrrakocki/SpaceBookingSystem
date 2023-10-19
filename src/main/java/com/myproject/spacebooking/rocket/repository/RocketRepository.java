package com.myproject.spacebooking.rocket.repository;

import com.myproject.spacebooking.rocket.model.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RocketRepository extends JpaRepository<Rocket, Long> {

    @Query("SELECT r FROM Rocket r WHERE r.name LIKE %:name%")
    List<Rocket> findByName(@Param("name") String name);
}
