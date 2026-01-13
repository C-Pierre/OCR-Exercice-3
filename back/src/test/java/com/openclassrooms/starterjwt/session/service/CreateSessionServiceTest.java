package com.openclassrooms.starterjwt.session.service;

import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.request.CreateSessionRequest;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CreateSessionServiceTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @Mock
    private TeacherRepositoryPort teacherRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private CreateSessionService createSessionService;

    @Test
    void should_create_session_with_users() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Yoga");
        request.setDescription("Morning yoga");
        request.setDate(new Date());
        request.setTeacherId(10L);
        request.setUsers(List.of(1L, 2L));

        Teacher teacher = new Teacher();
        teacher.setId(10L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        when(teacherRepositoryPort.getById(10L)).thenReturn(teacher);
        when(userRepositoryPort.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(user1, user2));

        SessionDto expectedDto = new SessionDto();
        when(sessionMapper.toDto(any(Session.class))).thenReturn(expectedDto);

        SessionDto result = createSessionService.execute(request);

        assertThat(result).isEqualTo(expectedDto);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepositoryPort).save(sessionCaptor.capture());

        Session savedSession = sessionCaptor.getValue();

        assertThat(savedSession.getName()).isEqualTo("Yoga");
        assertThat(savedSession.getDescription()).isEqualTo("Morning yoga");
        assertThat(savedSession.getTeacher()).isEqualTo(teacher);
        assertThat(savedSession.getUsers()).containsExactly(user1, user2);
        assertThat(savedSession.getCreatedAt()).isNotNull();
    }

    @Test
    void should_create_session_without_users() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Pilates");
        request.setDescription("Evening pilates");
        request.setDate(new Date());
        request.setTeacherId(5L);
        request.setUsers(null);

        Teacher teacher = new Teacher();
        teacher.setId(5L);

        when(teacherRepositoryPort.getById(5L)).thenReturn(teacher);

        SessionDto expectedDto = new SessionDto();
        when(sessionMapper.toDto(any(Session.class))).thenReturn(expectedDto);

        SessionDto result = createSessionService.execute(request);

        assertThat(result).isEqualTo(expectedDto);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepositoryPort).save(sessionCaptor.capture());

        Session savedSession = sessionCaptor.getValue();

        assertThat(savedSession.getName()).isEqualTo("Pilates");
        assertThat(savedSession.getTeacher()).isEqualTo(teacher);
        assertThat(savedSession.getUsers()).isNull();
        assertThat(savedSession.getCreatedAt()).isNotNull();

        verify(userRepositoryPort, never()).findAllById(any());
    }

    @Test
    void should_throw_when_teacher_not_found() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Boxing");
        request.setTeacherId(99L);

        when(teacherRepositoryPort.getById(99L)).thenThrow(new RuntimeException("Teacher not found"));

        assertThatThrownBy(() -> createSessionService.execute(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Teacher not found");
    }

    @Test
    void should_create_session_with_empty_users_list() {
        // GIVEN
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Dance");
        request.setTeacherId(1L);
        request.setUsers(List.of(100L)); // ID non existant

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherRepositoryPort.getById(1L)).thenReturn(teacher);
        when(userRepositoryPort.findAllById(request.getUsers())).thenReturn(List.of());

        SessionDto expectedDto = new SessionDto();
        when(sessionMapper.toDto(any(Session.class))).thenReturn(expectedDto);

        SessionDto result = createSessionService.execute(request);

        assertThat(result).isEqualTo(expectedDto);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepositoryPort).save(sessionCaptor.capture());

        Session savedSession = sessionCaptor.getValue();
        assertThat(savedSession.getUsers()).isEmpty();
    }

    @Test
    void should_map_saved_session_to_dto_correctly() {
        // GIVEN
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Yoga");
        request.setTeacherId(1L);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Session session = new Session();
        session.setName("Yoga");
        session.setTeacher(teacher);

        SessionDto dto = new SessionDto();

        when(teacherRepositoryPort.getById(1L)).thenReturn(teacher);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(dto);

        SessionDto result = createSessionService.execute(request);

        assertThat(result).isEqualTo(dto);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepositoryPort).save(sessionCaptor.capture());
        Session savedSession = sessionCaptor.getValue();
        assertThat(savedSession.getName()).isEqualTo("Yoga");

        assertThat(result).isEqualTo(dto);
        verify(sessionRepositoryPort).save(any(Session.class));
        verify(sessionMapper).toDto(any(Session.class));
    }

}
