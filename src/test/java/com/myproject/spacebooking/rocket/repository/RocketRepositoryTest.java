package com.myproject.spacebooking.rocket.repository;

import com.myproject.spacebooking.rocket.model.Rocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RocketRepositoryTest {

    private final RocketRepository underTest;

    @Autowired
    RocketRepositoryTest(RocketRepository underTest) {
        this.underTest = underTest;
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByName() {
        // given
        String name = "Rocket1";
        Rocket rocket = new Rocket(
                name,
                "Manufacturer",
                "capacity",
                "description"
        );
        underTest.save(rocket);

        // when
        List<Rocket> expected = underTest.findByName(name);

        //then
        assertEquals(1, expected.size());
        assertEquals(name, expected.get(0).getName());
    }

    @Test
    void itShouldNotFindByName() {
        // given
        String name = "Rocket1";
        Rocket rocket = new Rocket(
                "name",
                "Manufacturer",
                "capacity",
                "description"
        );
        underTest.save(rocket);

        // when
        List<Rocket> expected = underTest.findByName(name);

        //then
        assertThat(expected).isEmpty();
    }
}