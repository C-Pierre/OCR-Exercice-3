package com.openclassrooms.starterjwt.teacher.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.starterjwt.teacher.service.GetTeacherService;
import com.openclassrooms.starterjwt.teacher.service.GetTeachersService;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final GetTeacherService getTeacherService;
    private final GetTeachersService getTeachersService;

    public TeacherController(
        GetTeacherService getTeacherService,
        GetTeachersService getTeachersService
    ) {
        this.getTeacherService = getTeacherService;
        this.getTeachersService = getTeachersService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(getTeacherService.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> findAll() {
        return ResponseEntity.ok(getTeachersService.execute());
    }
}