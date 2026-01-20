package com.openclassrooms.starterjwt.session.repository.adapter;

import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class SessionRepositoryAdapterUnitTest {

    private SessionRepository sessionRepository;
    private SessionRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        adapter = new SessionRepositoryAdapter(sessionRepository);
    }

    @Test
    void getById_shouldReturnSession() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = adapter.getById(1L);
        assertThat(result).isEqualTo(session);
    }

    @Test
    void getById_shouldThrowNotFound() {
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> adapter.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Session not found");
    }

    @Test
    void save_shouldCallRepository() {
        Session session = new Session();
        adapter.save(session);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_shouldCallRepository() {
        Session session = new Session();
        adapter.delete(session);
        verify(sessionRepository).delete(session);
    }

    @Test
    void findAll_shouldReturnAll() {
        List<Session> list = List.of(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(list);

        List<Session> result = adapter.findAll();
        assertThat(result).hasSize(2);
    }

    // ---------------------- AJOUTS POUR COUVRIR 100% ----------------------

    @Test
    void findByNameAndTeacherId_shouldReturnOptional() {
        Session session = new Session();
        session.setId(10L);
        when(sessionRepository.findByNameAndTeacherId("S1", 1L))
                .thenReturn(Optional.of(session));

        Optional<Session> result = sessionRepository.findByNameAndTeacherId("S1", 1L);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(10L);
    }

    @Test
    void findByNameAndTeacherId_shouldReturnEmpty() {
        when(sessionRepository.findByNameAndTeacherId("S2", 2L))
                .thenReturn(Optional.empty());

        Optional<Session> result = sessionRepository.findByNameAndTeacherId("S2", 2L);
        assertThat(result).isEmpty();
    }

    @Test
    void deleteSessionParticipations_shouldCallRepository() {
        doNothing().when(sessionRepository).deleteSessionParticipations(5L);
        sessionRepository.deleteSessionParticipations(5L);
        verify(sessionRepository).deleteSessionParticipations(5L);
    }

    @Test
    void deleteAllSessionsParticipations_shouldCallRepository() {
        doNothing().when(sessionRepository).deleteAllSessionsParticipations();
        sessionRepository.deleteAllSessionsParticipations();
        verify(sessionRepository).deleteAllSessionsParticipations();
    }
}
