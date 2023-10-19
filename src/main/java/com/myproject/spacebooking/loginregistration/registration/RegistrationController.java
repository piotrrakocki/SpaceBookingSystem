package com.myproject.spacebooking.loginregistration.registration;

import com.myproject.spacebooking.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("webUser", new User());
        return "registration-form";
    }

    @PostMapping("/register")
    public String register(RegistrationRequest request, Model model) {
            registrationService.register(request);
        return "confirm-email";
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return "confirmed-email";
    }
}
