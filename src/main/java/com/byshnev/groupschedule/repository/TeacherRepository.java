package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
	Teacher findByUrlId(String urlId);
	List<Teacher> findBySurname(String surname);
	Teacher findByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);

	boolean existsByUrlId(String urlId);
	boolean existsByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);

	boolean deleteByUrlId(String urlId);
}
