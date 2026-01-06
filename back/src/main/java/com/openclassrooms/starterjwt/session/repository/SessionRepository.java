package com.openclassrooms.starterjwt.session.repository;

import org.springframework.stereotype.Repository;
import com.openclassrooms.starterjwt.session.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {}
