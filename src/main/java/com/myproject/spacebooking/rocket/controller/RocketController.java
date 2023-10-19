package com.myproject.spacebooking.rocket.controller;

import com.myproject.spacebooking.rocket.model.Rocket;
import com.myproject.spacebooking.rocket.repository.RocketRepository;
import com.myproject.spacebooking.rocket.service.RocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class RocketController {

    private final RocketService rocketService;

    private final RocketRepository rocketRepository;

    @Autowired
    public RocketController(RocketService rocketService, RocketRepository rocketRepository) {
        this.rocketService = rocketService;
        this.rocketRepository = rocketRepository;
    }

    @GetMapping("/rockets")
    public String getAllRockets(Model model) {
        List<Rocket> rockets = rocketService.getAllRockets();
        model.addAttribute("rockets", rockets);
        return "rockets";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/deleteRocket")
    public String deleteRocket(@RequestParam Long id) {
        log.info("Deleting rocket with id = {}", id);
        rocketService.deleteRocket(id);
        return "redirect:/rockets";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/addRockets")
    public String addEditRocket(@RequestParam(required = false, defaultValue = "-1") Long id, Model model) {
        log.info("editing id = {}", id);
        Optional<Rocket> rocket = rocketService.getRocketById(id);
        if (rocket == null) {
            rocket = Optional.of(new Rocket("","","",""));
        }

        model.addAttribute("rocket", rocket);
        model.addAttribute("error", model.getAttribute("error"));
        return "add_rockets";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/addRockets")
    public String processRocket(@ModelAttribute("rocket") Rocket rocket, Model model) {
        log.info("rocket from form = {}", rocket);
        try {
            if (rocket.getId() == null) {
                rocketService.addRocket(rocket);
            } else{
                rocketService.updateRocket(rocket);
            }
            return "redirect:/rockets";
        } catch (IllegalArgumentException e) {
            model.addAttribute("rocket", rocket);
            model.addAttribute("error","taken");
            return "add_rocket";
        }
    }
}
