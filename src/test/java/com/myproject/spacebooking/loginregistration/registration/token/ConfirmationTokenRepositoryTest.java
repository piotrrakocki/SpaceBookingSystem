package com.myproject.spacebooking.loginregistration.registration.token;

import com.myproject.spacebooking.user.model.Gender;
import com.myproject.spacebooking.user.model.User;
import com.myproject.spacebooking.user.model.UserRole;
import com.myproject.spacebooking.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ConfirmationTokenRepositoryTest {

    private final ConfirmationTokenRepository underTest;
    @Mock
    private UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepositoryTest(ConfirmationTokenRepository underTest) {
        this.underTest = underTest;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByToken() {
        // given
        String token = "token123";
        User user = new User(
                "firstName",
                "lastName",
                "email",
                "password",
                "phoneNumber",
                LocalDate.now(),
                Gender.MALE,
                UserRole.USER
        );

        ConfirmationToken confirmationToken =  new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        // when
        underTest.save(confirmationToken);
        Optional<ConfirmationToken> foundToken = underTest.findByToken(token);

        // then
        assertNotNull(foundToken);
        assertEquals(token, foundToken.get().getToken());
    }

    @Test
    void itShouldUpdateConfirmedAt() {
        // given
        LocalDateTime confirmedAt = LocalDateTime.now();
        String token = "token123";
        User user = new User(
                "firstName",
                "lastName",
                "email",
                "password",
                "phoneNumber",
                LocalDate.now(),
                Gender.MALE,
                UserRole.USER
        );

        ConfirmationToken confirmationToken =  new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        // when
        underTest.save(confirmationToken);
        int rowsUpdate = underTest.updateConfirmedAt(token, confirmedAt);

        // then
        assertEquals(1, rowsUpdate, "One row should be updated");
    }
}