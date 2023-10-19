package com.myproject.spacebooking.loginregistration.registration;

import com.myproject.spacebooking.loginregistration.email.EmailSender;
import com.myproject.spacebooking.loginregistration.registration.token.ConfirmationToken;
import com.myproject.spacebooking.loginregistration.registration.token.ConfirmationTokenService;
import com.myproject.spacebooking.user.model.Gender;
import com.myproject.spacebooking.user.model.User;
import com.myproject.spacebooking.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailSender emailSender;

    private RegistrationService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RegistrationService(userService, emailValidator, confirmationTokenService, emailSender);
    }

    @Test
    void willThrowEmailNotValid() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "firstName",
                "lastName",
                "email",
                "password",
                "123 456 789",
                LocalDate.now(),
                Gender.MALE
        );
        given(emailValidator.test(request.getEmail())).willReturn(false);

        // when
        assertThatThrownBy(() -> underTest.register(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email not valid");

        // then
        verify(userService, never()).singUpUser(new User());
        verify(emailSender, never()).send(request.getEmail(), "");
    }

    @Test
    void canRegister() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "firstName",
                "lastName",
                "email",
                "password",
                "123 456 789",
                LocalDate.now(),
                Gender.MALE
        );

        // when
        when(emailValidator.test(request.getEmail())).thenReturn(true);
        when(userService.singUpUser(any(User.class))).thenReturn("token");

        String token = underTest.register(request);

        // then
        assertNotNull(token);
        verify(emailSender, times(1)).send(eq(request.getEmail()), anyString());
    }

    @Test
    void willThrowTokenNotFound() {
        // when
        assertThatThrownBy(() -> underTest.confirmToken("token"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("token not found");

        // then
        verify(confirmationTokenService, never()).setConfirmedAt("token");
        verify(userService, never()).enableUser("email");
    }

    @Test
    void willThrowEmailAlreadyConfirmed() {
        // given
        String token = "test-token";
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().plusMinutes(15),
                new User()
        );
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        // when
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            underTest.confirmToken(token);
        });

        // then
        String expectedMessage = "email already confirmed";
        String actualMessage = exception.getMessage();
        assert(actualMessage.contains(expectedMessage));
        verify(confirmationTokenService, never()).setConfirmedAt("token");
        verify(userService, never()).enableUser("email");
    }

    @Test
    void willThrowTokenExpired() {
        // given
        String token = "test-token";
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().minusMinutes(15),
                new User()
        );

        // when
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            underTest.confirmToken(token);
        });

        // then
        String expectedMessage = "token expired";
        String actualMessage = exception.getMessage();
        assert(actualMessage.contains(expectedMessage));
    }

    @Test
    void willConfirmToken() {
        // given
        String token = "test-token";
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().plusMinutes(15),
                new User()
        );

        // when
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        underTest.confirmToken(token);

        // then
        verify(confirmationTokenService).setConfirmedAt(token);
        verify(userService).enableUser(confirmationToken.getUser().getEmail());
    }
}