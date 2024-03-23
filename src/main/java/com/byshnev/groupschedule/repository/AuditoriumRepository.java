package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
	Auditorium findByAuditorium(String auditorium);
}
