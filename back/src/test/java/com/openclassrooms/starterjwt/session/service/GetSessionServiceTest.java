package com.openclassrooms.starterjwt.session.service;

import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GetSessionServiceTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @InjectMocks
    private GetSessionService getSessionService;

    @Test
    void should_return_sessionDto_for_given_id() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        SessionDto mockDto = new SessionDto();
        mockDto.setId(sessionId);

        when(sessionRepositoryPort.getById(sessionId)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockDto);

        SessionDto result = getSessionService.execute(sessionId);

        verify(sessionRepositoryPort).getById(sessionId);
        verify(sessionMapper).toDto(mockSession);
        assertThat(result).isEqualTo(mockDto);
    }

    @Test
    void should_throw_exception_if_session_not_found() {
        Long sessionId = 99L;

        when(sessionRepositoryPort.getById(sessionId))
            .thenThrow(new RuntimeException("Session not found"));

        assertThatThrownBy(() -> getSessionService.execute(sessionId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Session not found");

        verify(sessionRepositoryPort).getById(sessionId);
    }
}
