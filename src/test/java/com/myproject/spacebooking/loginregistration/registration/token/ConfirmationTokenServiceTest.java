package com.myproject.spacebooking.loginregistration.registration.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock
    private Clock clock;
    private ConfirmationTokenService underTest;

    private static ZonedDateTime NOW = ZonedDateTime.of(
            2023,
            10,
            15,
            12,
            30,
            30,
            0,
            ZoneId.of("GMT")
    );

    @BeforeEach
    void setUp() {
        underTest = new ConfirmationTokenService(confirmationTokenRepository, clock);
    }

    @Test
    void canSaveConfirmationToken() {
        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();

        // when
        underTest.saveConfirmationToken(confirmationToken);

        // then
        ArgumentCaptor<ConfirmationToken> confirmationTokenArgumentCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenRepository).save(confirmationTokenArgumentCaptor.capture());

        ConfirmationToken capturedConfirmationToken = confirmationTokenArgumentCaptor.getValue();
        assertThat(capturedConfirmationToken).isEqualTo(confirmationToken);
    }

    @Test
    void canGetToken() {
        // given
        String token = "token123";
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        given(confirmationTokenRepository.findByToken(token)).willReturn(Optional.of(confirmationToken));

        // when
        Optional<ConfirmationToken> result = underTest.getToken(token);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(confirmationToken);
    }

    @Test
    void canSetConfirmedAt() {
        // given
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());
        String token = "token123";
        LocalDateTime confirmedAt = LocalDateTime.of(
                2023,
                10,
                15,
                12,
                30,
                30,
                0
        );


        // when
        when(confirmationTokenRepository.updateConfirmedAt(token, confirmedAt)).thenReturn(1);

        int result = underTest.setConfirmedAt(token);

        // then
        assertEquals(1, result, "One row should be updated");
        verify(confirmationTokenRepository).updateConfirmedAt(token, confirmedAt);
    }
}