package com.openclassrooms.starterjwt.session.service;

import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ParticipateSessionServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private ParticipateSessionService service;

    @Test
    void should_add_user_if_not_already_participating() {
        // GIVEN
        User user1 = new User();
        user1.setId(1L);

        Session session = new Session();
        session.setId(100L);
        session.setUsers(new ArrayList<>()); // aucun participant initialement

        when(sessionRepositoryPort.getById(100L)).thenReturn(session);
        when(userRepositoryPort.getById(1L)).thenReturn(user1);

        // WHEN
        service.execute(100L, 1L);

        // THEN
        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getUsers()).contains(user1);
        verify(sessionRepositoryPort).save(session);
    }

    @Test
    void should_throw_exception_if_user_already_participates() {
        // GIVEN
        User user1 = new User();
        user1.setId(1L);

        Session session = new Session();
        session.setId(100L);
        session.setUsers(new ArrayList<>(List.of(user1))); // utilisateur déjà présent

        when(sessionRepositoryPort.getById(100L)).thenReturn(session);
        when(userRepositoryPort.getById(1L)).thenReturn(user1);

        // WHEN / THEN
        assertThatThrownBy(() -> service.execute(100L, 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User already participates in this session");

        verify(sessionRepositoryPort, never()).save(any());
    }
}
