package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Date;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {
	Date findByDateValue(LocalDate date);
}
