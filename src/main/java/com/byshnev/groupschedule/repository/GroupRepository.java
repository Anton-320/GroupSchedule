package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.StudentGroup;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends JpaRepository<StudentGroup, Integer> {
	StudentGroup findByGroupNum(Integer groupNum);
}
