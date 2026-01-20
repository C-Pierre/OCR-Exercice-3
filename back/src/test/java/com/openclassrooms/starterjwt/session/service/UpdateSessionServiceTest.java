package com.openclassrooms.starterjwt.session.service;

import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.session.request.UpdateSessionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UpdateSessionServiceTest {

    @Mock
    private SessionRepositoryPort sessionRepositoryPort;

    @Mock
    private TeacherRepositoryPort teacherRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private UpdateSessionService service;

    @Test
    void should_update_session_when_fields_changed() {
        // GIVEN
        Session session = new Session();
        session.setId(1L);
        session.setName("Old Name");
        session.setDescription("Old Description");
        session.setUsers(new ArrayList<>());
        session.setTeacher(null);
        session.setDate(new Date());

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("New Name");
        request.setDescription("New Description");
        request.setDate(new Date());
        request.setTeacherId(10L);
        request.setUsers(List.of(1L,2L));

        Teacher teacher = new Teacher();
        teacher.setId(10L);

        List<User> newUsers = List.of(new User(){ { setId(1L); } }, new User(){ { setId(2L); } });
        SessionDto dto = new SessionDto();

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(teacherRepositoryPort.getById(10L)).thenReturn(teacher);
        when(userRepositoryPort.findAllById(request.getUsers())).thenReturn(newUsers);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        // WHEN
        SessionDto result = service.execute(1L, request);

        // THEN
        assertThat(session.getName()).isEqualTo("New Name");
        assertThat(session.getDescription()).isEqualTo("New Description");
        assertThat(session.getDate()).isEqualTo(request.getDate());
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).isEqualTo(newUsers);
        verify(sessionRepositoryPort).save(session);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void should_not_save_when_nothing_changed() {
        // GIVEN
        Teacher teacher = new Teacher();
        teacher.setId(10L);
        List<User> users = List.of(new User(){ { setId(1L); } });

        Date now = new Date();

        Session session = new Session();
        session.setId(1L);
        session.setName("Name");
        session.setDescription("Desc");
        session.setDate(now);
        session.setTeacher(teacher);
        session.setUsers(users);

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("Name");
        request.setDescription("Desc");
        request.setDate(now);
        request.setTeacherId(10L);
        request.setUsers(List.of(1L));

        SessionDto dto = new SessionDto();

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(userRepositoryPort.findAllById(request.getUsers())).thenReturn(users);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        // WHEN
        SessionDto result = service.execute(1L, request);

        // THEN
        verify(sessionRepositoryPort, never()).save(any());
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void should_not_modify_name_when_requestNameIsNull() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Old Name");

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName(null);

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        SessionDto dto = service.execute(1L, request);

        assertThat(session.getName()).isEqualTo("Old Name");
        verify(sessionRepositoryPort, never()).save(any());
        assertThat(dto).isNotNull();
    }

    @Test
    void should_not_modify_description_when_equal() {
        Session session = new Session();
        session.setId(1L);
        session.setDescription("Same Desc");

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setDescription("Same Desc");

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        SessionDto dto = service.execute(1L, request);

        assertThat(session.getDescription()).isEqualTo("Same Desc");
        verify(sessionRepositoryPort, never()).save(any());
    }

    @Test
    void should_modify_teacher_when_teacherChanged() {
        Session session = new Session();
        session.setId(1L);
        Teacher oldTeacher = new Teacher();
        oldTeacher.setId(5L);
        session.setTeacher(oldTeacher);

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setTeacherId(10L);

        Teacher newTeacher = new Teacher();
        newTeacher.setId(10L);

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(teacherRepositoryPort.getById(10L)).thenReturn(newTeacher);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        SessionDto dto = service.execute(1L, request);

        assertThat(session.getTeacher()).isEqualTo(newTeacher);
        verify(sessionRepositoryPort).save(session);
    }

    @Test
    void should_add_teacher_when_sessionTeacherIsNull() {
        Session session = new Session();
        session.setId(1L);
        session.setTeacher(null);

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setTeacherId(10L);

        Teacher newTeacher = new Teacher();
        newTeacher.setId(10L);

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(teacherRepositoryPort.getById(10L)).thenReturn(newTeacher);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        SessionDto dto = service.execute(1L, request);

        assertThat(session.getTeacher()).isEqualTo(newTeacher);
        verify(sessionRepositoryPort).save(session);
    }

    @Test
    void should_not_modify_teacher_when_teacherIdEqualsSessionTeacher() {
        Session session = new Session();
        session.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(10L);
        session.setTeacher(teacher);

        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setTeacherId(10L);

        when(sessionRepositoryPort.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        SessionDto dto = service.execute(1L, request);

        assertThat(session.getTeacher()).isEqualTo(teacher);
        verify(sessionRepositoryPort, never()).save(any());
    }

}
