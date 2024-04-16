package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GroupRepository.
 * */
@Repository
public interface GroupRepository extends JpaRepository<StudentGroup, Integer> {
  StudentGroup findByGroupNumber(Integer groupNumber);
}
