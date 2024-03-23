package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<StudentGroup, Long> {
	StudentGroup findByGroupNum(Integer groupNum);
}
