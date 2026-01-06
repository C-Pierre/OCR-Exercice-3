package com.openclassrooms.starterjwt.teacher.repository;

import org.springframework.stereotype.Repository;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {}
