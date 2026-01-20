package com.openclassrooms.starterjwt.session.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.openclassrooms.starterjwt.session.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByNameAndTeacherId(String name, Long teacherId);

    @Modifying
    @Query(value = "DELETE FROM `participate` WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSessionParticipations(@Param("sessionId") Long sessionId);

    @Modifying
    @Query(value = "DELETE FROM `participate`;", nativeQuery = true)
    void deleteAllSessionsParticipations();
}