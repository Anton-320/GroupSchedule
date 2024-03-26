package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<StudentGroup, Integer> {
	StudentGroup findByGroupNum(Integer groupNum);
}
