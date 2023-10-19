package com.myproject.spacebooking.security.config;

import com.myproject.spacebooking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(configure ->
                        configure
                                .requestMatchers("/addRockets").hasAnyAuthority("ADMIN")
                                .requestMatchers("/deleteRocket").hasAnyAuthority("ADMIN")
                                .requestMatchers("/addFlights").hasAnyAuthority("ADMIN")
                                .requestMatchers("/deleteFlight").hasAnyAuthority("ADMIN")
                                .requestMatchers("/profile/**").hasAnyAuthority("ADMIN","USER")
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/rockets").permitAll()
                                .requestMatchers("/flights").permitAll()
                                .requestMatchers("/review").permitAll()
                                .requestMatchers("/api/v1/registration/*").permitAll()
                                .requestMatchers("/api/v1/registration/register").permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/fragments/*", "/kosmos.webp","/home.css" , "/flights.css", "/add_flights.css", "/rockets.css", "/add_rockets.css", "/profile.css", "/review.css").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .formLogin(form ->
                        form
                                .defaultSuccessUrl("/home",true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .permitAll()
                                .logoutSuccessUrl("/home"));
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider()));
    }
}
