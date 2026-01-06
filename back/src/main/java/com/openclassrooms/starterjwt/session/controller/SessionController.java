package com.openclassrooms.starterjwt.session.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.session.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.starterjwt.session.request.CreateSessionRequest;
import com.openclassrooms.starterjwt.session.request.UpdateSessionRequest;

@RestController
@RequestMapping("/api/session")
@Log4j2
public class SessionController {

    private final GetSessionService getSessionService;
    private final GetSessionsService getSessionsService;
    private final CreateSessionService createSessionService;
    private final DeleteSessionService deleteSessionService;
    private final UpdateSessionService updateSessionService;
    private final ParticipateSessionService participateSessionService;
    private final NoLongerParticipateSessionService noLongerParticipateSessionService;

    public SessionController(
        GetSessionService getSessionService,
        GetSessionsService getSessionsService,
        CreateSessionService createSessionService,
        DeleteSessionService deleteSessionService,
        UpdateSessionService updateSessionService,
        ParticipateSessionService participateSessionService,
        NoLongerParticipateSessionService noLongerParticipateSessionService
    ) {
        this.getSessionService = getSessionService;
        this.getSessionsService = getSessionsService;
        this.createSessionService = createSessionService;
        this.deleteSessionService = deleteSessionService;
        this.updateSessionService = updateSessionService;
        this.participateSessionService = participateSessionService;
        this.noLongerParticipateSessionService = noLongerParticipateSessionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getSessionService.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<SessionDto>> findAll() {
        return ResponseEntity.ok(getSessionsService.execute());
    }

    @PostMapping
    public ResponseEntity<SessionDto> create(
        @Valid @RequestBody CreateSessionRequest request
    ) {
        return ResponseEntity.status(201).body(createSessionService.execute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateSessionRequest request
    ) {
        return ResponseEntity.ok(updateSessionService.execute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteSessionService.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/participate/{userId}")
    public ResponseEntity<Void> participate(@PathVariable Long id, @PathVariable Long userId) {
        participateSessionService.execute(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/participate/{userId}")
    public ResponseEntity<Void> noLongerParticipate(@PathVariable Long id, @PathVariable Long userId) {
        noLongerParticipateSessionService.execute(id, userId);
        return ResponseEntity.ok().build();
    }
}