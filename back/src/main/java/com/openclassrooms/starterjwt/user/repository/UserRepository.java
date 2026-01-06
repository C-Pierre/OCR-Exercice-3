package com.openclassrooms.starterjwt.user.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.openclassrooms.starterjwt.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
