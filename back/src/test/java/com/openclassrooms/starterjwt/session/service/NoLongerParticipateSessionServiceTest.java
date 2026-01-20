package com.openclassrooms.starterjwt.session.service;

import org.mockito.*;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class NoLongerParticipateSessionServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @InjectMocks
    private NoLongerParticipateSessionService service;

    @Test
    void should_remove_user_if_participating() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        Session session = new Session();
        session.setId(100L);
        session.setUsers(new ArrayList<>(List.of(user1, user2)));

        when(sessionRepositoryPort.getById(100L)).thenReturn(session);

        service.execute(100L, 1L);

        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getUsers()).doesNotContain(user1);
        verify(sessionRepositoryPort).save(session);
    }

    @Test
    void should_throw_exception_if_user_not_participating() {
        User user2 = new User();
        user2.setId(2L);

        Session session = new Session();
        session.setId(100L);
        session.setUsers(new ArrayList<>(List.of(user2)));

        when(sessionRepositoryPort.getById(100L)).thenReturn(session);

        assertThatThrownBy(() -> service.execute(100L, 1L))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("User does not participate in this session");

        verify(sessionRepositoryPort, never()).save(any());
    }
}
