package com.openclassrooms.starterjwt.user.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.openclassrooms.starterjwt.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
