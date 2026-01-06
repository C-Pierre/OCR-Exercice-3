package com.openclassrooms.starterjwt.security.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepositoryPort userRepositoryPort;

    UserDetailsServiceImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositoryPort.getByEmail(username);

        return UserDetailsImpl
            .builder()
            .id(user.getId())
            .username(user.getEmail())
            .lastName(user.getLastName())
            .firstName(user.getFirstName())
            .password(user.getPassword())
            .build();
    }
}
