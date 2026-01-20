package com.openclassrooms.starterjwt.session.service;

import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.List;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GetSessionsServiceTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @InjectMocks
    private GetSessionsService getSessionsService;

    @Test
    void should_return_list_of_sessionDto() {
        Session session1 = new Session(); session1.setId(1L);
        Session session2 = new Session(); session2.setId(2L);

        SessionDto dto1 = new SessionDto(); dto1.setId(1L);
        SessionDto dto2 = new SessionDto(); dto2.setId(2L);

        List<Session> mockSessions = List.of(session1, session2);

        when(sessionRepositoryPort.findAll()).thenReturn(mockSessions);
        when(sessionMapper.toDto(session1)).thenReturn(dto1);
        when(sessionMapper.toDto(session2)).thenReturn(dto2);

        List<SessionDto> result = getSessionsService.execute();

        verify(sessionRepositoryPort).findAll();
        verify(sessionMapper).toDto(session1);
        verify(sessionMapper).toDto(session2);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void should_return_empty_list_if_no_sessions() {
        when(sessionRepositoryPort.findAll()).thenReturn(new ArrayList<>());

        List<SessionDto> result = getSessionsService.execute();

        verify(sessionRepositoryPort).findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void should_call_mapper_for_each_session() {
        Session session1 = new Session(); session1.setId(1L);
        Session session2 = new Session(); session2.setId(2L);
        List<Session> mockSessions = List.of(session1, session2);

        when(sessionRepositoryPort.findAll()).thenReturn(mockSessions);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(new SessionDto());

        getSessionsService.execute();

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionMapper, times(2)).toDto(captor.capture());

        List<Session> capturedSessions = captor.getAllValues();
        assertThat(capturedSessions).containsExactly(session1, session2);
    }

    @Test
    void should_throw_exception_if_repository_fails() {
        when(sessionRepositoryPort.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> getSessionsService.execute())
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("DB error");

        verify(sessionRepositoryPort).findAll();
    }
}
