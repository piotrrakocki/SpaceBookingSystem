package com.myproject.spacebooking.user.repository;

import com.myproject.spacebooking.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    private final UserRepository underTest;

    @Autowired
    UserRepositoryTest(UserRepository underTest) {
        this.underTest = underTest;
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShoudfindByEmail() {
        // given
        String email = "example@email.com";
        User user = new User();
        user.setEmail(email);
        underTest.save(user);

        // when
        Optional<User> expected = underTest.findByEmail(email);

        // then
        assertEquals(Optional.of(user), expected);
        assertEquals(expected.get().getEmail(), email);
    }

    @Test
    void itShoudEnableAppUser() {
        // given
        String email = "example.email.com";
        User user = new User();
        user.setEmail(email);
        underTest.save(user);

        // when
        int expected = underTest.enableAppUser(email);

        // then
        assertEquals(1, expected);
    }
}