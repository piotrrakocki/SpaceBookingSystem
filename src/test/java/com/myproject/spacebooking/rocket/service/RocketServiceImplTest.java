package com.myproject.spacebooking.rocket.service;

import com.myproject.spacebooking.exceptions.RocketNotFoundException;
import com.myproject.spacebooking.rocket.model.Rocket;
import com.myproject.spacebooking.rocket.repository.RocketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RocketServiceImplTest {

    @Mock
    private RocketRepository rocketRepository;
    private RocketServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new RocketServiceImpl(rocketRepository);
    }

    @Test
    void canGetAllRockets() {
        // when
        underTest.getAllRockets();

        // then
        verify(rocketRepository).findAll();
    }

    @Test
    void canGetRocketById() {
        // given
        long id = 10;
        Rocket rocket = new Rocket();
        rocket.setId(id);
        given(rocketRepository.findById(id)).willReturn(Optional.of(rocket));

        // when
        Optional<Rocket> result = underTest.getRocketById(id);

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(rocket);
    }

    @Test
    void canAddRocket() {
        // given
        Rocket rocket = new Rocket();

        // when
        underTest.addRocket(rocket);

        // then
        ArgumentCaptor<Rocket> rocketArgumentCaptor = ArgumentCaptor.forClass(Rocket.class);
        verify(rocketRepository).save(rocketArgumentCaptor.capture());

        Rocket capturedRocket = rocketArgumentCaptor.getValue();
        assertThat(capturedRocket).isEqualTo(rocket);
    }

    @Test
    void canUpdateRocket() {
        // given
        Rocket rocket = new Rocket();

        // when
        underTest.updateRocket(rocket);

        // then
        ArgumentCaptor<Rocket> rocketArgumentCaptor = ArgumentCaptor.forClass(Rocket.class);
        verify(rocketRepository).save(rocketArgumentCaptor.capture());

        Rocket capturedRocket = rocketArgumentCaptor.getValue();
        assertThat(capturedRocket).isEqualTo(rocket);

    }

    @Test
    void canDeleteRocket() {
        // given
        long id = 10;
        given(rocketRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteRocket(id);

        // then
        verify(rocketRepository).deleteById(id);

    }

    @Test
    void willThrowRocketNotFoundException() {
        // given
        long id = 10;
        given(rocketRepository.existsById(id)).willReturn(false);

        // when
        assertThatThrownBy(() -> underTest.deleteRocket(id))
                .isInstanceOf(RocketNotFoundException.class)
                .hasMessageContaining("Rocket with ID " + id + " does not exist.");

        // then
        verify(rocketRepository, never()).deleteById(id);
    }

}