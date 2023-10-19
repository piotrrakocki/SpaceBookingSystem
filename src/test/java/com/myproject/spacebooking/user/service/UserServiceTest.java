package com.myproject.spacebooking.user.service;

import com.myproject.spacebooking.exceptions.EmailAlreadyTakenException;
import com.myproject.spacebooking.loginregistration.registration.token.ConfirmationToken;
import com.myproject.spacebooking.loginregistration.registration.token.ConfirmationTokenService;
import com.myproject.spacebooking.user.model.User;
import com.myproject.spacebooking.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, bCryptPasswordEncoder, confirmationTokenService);
    }

    @Test
    void willThrowUsernameNotFoundException() {
        // given
        String email = "example@email.com";

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("user with email %s not found", email);
    }

    @Test
    void canLoadUserByUsername() {
        // given
        String email = "example@email.com";
        User user = new User();
        user.setEmail(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        UserDetails expected = underTest.loadUserByUsername(email);

        // then
        assertEquals(user, expected);
    }

    @Test
    void willThrowEmailAlreadyTaken() {
        // given
        String email = "example@email.com";
        User user = new User();
        user.setEmail(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        assertThatThrownBy(() -> underTest.singUpUser(user))
                .isInstanceOf(EmailAlreadyTakenException.class)
                .hasMessageContaining("email already taken");

        // then
        verify(bCryptPasswordEncoder, never()).encode(user.getPassword());
        verify(userRepository, never()).save(user);
        verify(confirmationTokenService, never()).saveConfirmationToken(new ConfirmationToken());
    }

    @Test
    void canSingUpUser() {
        // given
        String email = "example@email.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        String token = underTest.singUpUser(user);

        // then
        assertNotNull(token);
    }

    @Test
    void canEnableUser() {
        // given
        String email = "example@email.com";
        User user = new User();
        user.setEmail(email);

        // when
        when(userRepository.enableAppUser(email)).thenReturn(1);

        int exepted = underTest.enableUser(email);

        // then
        assertEquals(1, exepted);

    }
}

