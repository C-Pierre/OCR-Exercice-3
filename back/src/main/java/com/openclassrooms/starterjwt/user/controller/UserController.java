package com.openclassrooms.starterjwt.user.controller;

import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.user.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.starterjwt.user.service.GetUserService;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.starterjwt.user.service.DeleteUserService;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final DeleteUserService deleteUserService;
    private final GetUserService getUserService;

    public UserController(
        DeleteUserService deleteUserService,
        GetUserService getUserService
    ) {
        this.deleteUserService = deleteUserService;
        this.getUserService = getUserService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getUserService.execute(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        deleteUserService.execute(id, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}
