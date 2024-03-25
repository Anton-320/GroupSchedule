package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Auditorium;
import lombok.NonNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
	Auditorium findByAuditorium(String auditorium);

	boolean existsById(long id);
	boolean existsByAuditorium(String auditorium);
}
