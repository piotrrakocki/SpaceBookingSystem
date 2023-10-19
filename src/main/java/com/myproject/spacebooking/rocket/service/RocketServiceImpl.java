package com.myproject.spacebooking.rocket.service;

import com.myproject.spacebooking.exceptions.RocketNotFoundException;
import com.myproject.spacebooking.rocket.model.Rocket;
import com.myproject.spacebooking.rocket.repository.RocketRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RocketServiceImpl implements RocketService {

    private final RocketRepository rocketRepository;

    @Autowired
    public RocketServiceImpl(RocketRepository rocketRepository) {
        this.rocketRepository = rocketRepository;
    }

    @Override
    public List<Rocket> getAllRockets() {
        return rocketRepository.findAll();
    }

    @Override
    public Optional<Rocket> getRocketById(Long rocketId) {
        return rocketRepository.findById(rocketId);
    }

    @Override
    public void addRocket(@NonNull Rocket rocket) {
        rocketRepository.save(rocket);
    }


    @Override
    public void updateRocket(Rocket rocket) {
        rocketRepository.save(rocket);
    }

    @Override
    public void deleteRocket(Long id) {
        if (rocketRepository.existsById(id)) {
            rocketRepository.deleteById(id);
        } else {
            throw new RocketNotFoundException("Rocket with ID " + id + " does not exist.");
        }

    }
}
