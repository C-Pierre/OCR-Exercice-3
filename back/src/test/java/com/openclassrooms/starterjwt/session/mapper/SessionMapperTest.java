package com.openclassrooms.starterjwt.session.mapper;

import java.util.Date;
import java.util.List;

import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.user.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    void toDto_shouldMapAllFields() {
        Teacher teacher = new Teacher();
        teacher.setId(10L);

        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);

        Session session = new Session();
        session.setId(100L);
        session.setName("Spring Boot");
        session.setDescription("Test session");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(List.of(u1, u2));

        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getName()).isEqualTo("Spring Boot");
        assertThat(dto.getDescription()).isEqualTo("Test session");
        assertThat(dto.getTeacherId()).isEqualTo(10L);
        assertThat(dto.getUsers()).hasSize(2);
        assertThat(dto.getUsers().get(0).getId()).isEqualTo(1L);
        assertThat(dto.getUsers().get(1).getId()).isEqualTo(2L);
    }

    @Test
    void toDto_shouldHandleNullUsersAndTeacher() {
        Session session = new Session();
        session.setId(101L);
        session.setTeacher(null); // teacher null
        session.setUsers(null);   // users null

        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto.getUsers()).isNotNull().isEmpty();
        assertThat(dto.getTeacherId()).isNull();
    }

    @Test
    void toEntity_shouldMapAllFieldsIncludingNulls() {
        SessionDto dto = new SessionDto();
        dto.setId(200L);
        dto.setName("Yoga");
        dto.setDescription("DTO to entity test");
        dto.setTeacherId(20L);

        Session session = sessionMapper.toEntity(dto);

        session.setTeacher(null);

        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(200L);
        assertThat(session.getName()).isEqualTo("Yoga");
        assertThat(session.getDescription()).isEqualTo("DTO to entity test");
        assertThat(session.getTeacher()).isNull();

        // Cas DTO avec nulls
        SessionDto dtoNull = new SessionDto();
        dtoNull.setId(201L);
        dtoNull.setTeacherId(null);
        dtoNull.setName(null);
        dtoNull.setDescription(null);

        Session sessionNull = sessionMapper.toEntity(dtoNull);
        assertThat(sessionNull).isNotNull();
        assertThat(sessionNull.getId()).isEqualTo(201L);
        assertThat(sessionNull.getName()).isNull();
        assertThat(sessionNull.getDescription()).isNull();
        assertThat(sessionNull.getTeacher()).isNull();
    }

    @Test
    void toDtoList_shouldMapMultipleSessions() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Session s1 = new Session();
        s1.setId(100L);
        s1.setTeacher(teacher);

        Session s2 = new Session();
        s2.setId(101L);
        s2.setTeacher(teacher);

        List<SessionDto> dtos = sessionMapper.toDto(List.of(s1, s2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(100L);
        assertThat(dtos.get(1).getId()).isEqualTo(101L);
    }

    @Test
    void toEntityList_shouldMapMultipleDtosIncludingNullTeacher() {
        SessionDto d1 = new SessionDto();
        d1.setId(200L);
        d1.setTeacherId(10L);
        d1.setName("Test");
        d1.setDescription("Description");

        SessionDto d2 = new SessionDto();
        d2.setId(201L);
        d2.setTeacherId(null); // couverture branche null
        d2.setName(null);
        d2.setDescription(null);

        List<Session> sessions = List.of(sessionMapper.toEntity(d1), sessionMapper.toEntity(d2));

        assertThat(sessions).hasSize(2);
        assertThat(sessions.get(0).getId()).isEqualTo(200L);
        assertThat(sessions.get(1).getId()).isEqualTo(201L);
        assertThat(sessions.get(1).getTeacher()).isNull();
        assertThat(sessions.get(1).getName()).isNull();
        assertThat(sessions.get(1).getDescription()).isNull();
    }

    @Test
    void toEntity_shouldHandleTeacherId() {
        // DTO avec teacherId
        SessionDto dtoWithTeacher = new SessionDto();
        dtoWithTeacher.setId(300L);
        dtoWithTeacher.setName("Test1");
        dtoWithTeacher.setDescription("Desc1");
        dtoWithTeacher.setTeacherId(10L);

        Session session = sessionMapper.toEntity(dtoWithTeacher);

        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(300L);
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getTeacher().getId()).isEqualTo(10L);

        // DTO sans teacherId
        SessionDto dtoWithoutTeacher = new SessionDto();
        dtoWithoutTeacher.setId(301L);
        dtoWithoutTeacher.setName("Test2");
        dtoWithoutTeacher.setDescription("Desc2");
        dtoWithoutTeacher.setTeacherId(null);

        Session session2 = sessionMapper.toEntity(dtoWithoutTeacher);
        assertThat(session2.getTeacher()).isNull();
    }

    @Test
    void toEntityList_shouldMapListAndHandleNull() {
        SessionDto dto1 = new SessionDto();
        dto1.setId(400L);
        dto1.setTeacherId(5L);

        SessionDto dto2 = new SessionDto();
        dto2.setId(401L);
        dto2.setTeacherId(null);

        List<Session> list = sessionMapper.toEntity(List.of(dto1, dto2));
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getTeacher()).isNotNull();
        assertThat(list.get(1).getTeacher()).isNull();

        List<Session> emptyList = sessionMapper.toEntity((List<SessionDto>) null);
        assertThat(emptyList).isEmpty();
    }

    @Test
    void toDto_shouldReturnNull_whenSessionIsNull() {
        SessionDto dto = sessionMapper.toDto((Session) null);
        assertThat(dto).isNull();
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        Session session = sessionMapper.toEntity((SessionDto) null);
        assertThat(session).isNull();
    }

    @Test
    void toDtoList_shouldReturnEmptyList_whenListIsNull() {
        List<SessionDto> dtos = sessionMapper.toDto((List<Session>) null);
        assertThat(dtos).isNotNull().isEmpty();
    }

    @Test
    void toEntityList_shouldReturnEmptyList_whenListIsNull() {
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);
        assertThat(sessions).isNotNull().isEmpty();
    }
}
