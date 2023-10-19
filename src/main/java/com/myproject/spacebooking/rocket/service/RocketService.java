package com.myproject.spacebooking.rocket.service;

import com.myproject.spacebooking.rocket.model.Rocket;

import java.util.List;
import java.util.Optional;

public interface RocketService {

    List<Rocket> getAllRockets();

    Optional<Rocket> getRocketById(Long rocketId);

    void addRocket(Rocket rocket);

    void updateRocket(Rocket rocket);

    void deleteRocket(Long id);
}
