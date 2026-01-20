package com.openclassrooms.starterjwt.session.service;

import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DeleteSessionServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @InjectMocks
    private DeleteSessionService deleteSessionService;

    @Test
    void should_delete_session_by_id() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        when(sessionRepositoryPort.getById(sessionId)).thenReturn(mockSession);

        deleteSessionService.execute(sessionId);

        verify(sessionRepositoryPort).getById(sessionId);
        verify(sessionRepositoryPort).delete(mockSession);
    }

    @Test
    void should_throw_exception_if_session_not_found() {
        Long sessionId = 99L;

        when(sessionRepositoryPort.getById(sessionId)).thenThrow(new RuntimeException("Session not found"));

        assertThatThrownBy(() -> deleteSessionService.execute(sessionId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Session not found");

        verify(sessionRepositoryPort).getById(sessionId);
        verify(sessionRepositoryPort, never()).delete(any());
    }

    @Test
    void should_delete_session_with_users_and_teacher() {
        Session session = new Session();
        session.setId(1L);
        session.setUsers(List.of(new com.openclassrooms.starterjwt.user.model.User(){{
            setId(1L);
        }}));
        session.setTeacher(new com.openclassrooms.starterjwt.teacher.model.Teacher(){{
            setId(10L);
        }});

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);

        deleteSessionService.execute(1L);

        verify(sessionRepositoryPort).delete(session);
    }
}
