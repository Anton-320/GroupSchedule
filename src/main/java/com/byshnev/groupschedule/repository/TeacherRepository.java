package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.model.entity.compositekey.FullName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, FullName> {
	Teacher findByFullname(FullName fullName);
}
