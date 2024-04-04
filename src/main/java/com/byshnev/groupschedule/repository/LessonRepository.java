package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

	Optional<Lesson> findLessonByGroupGroupNumAndDateAndStartTime(Integer group, LocalDate date, LocalTime startTime);

	List<Lesson> findByGroupGroupNum(Integer groupNum);
	List<Lesson> findLessonsByGroupGroupNum(Integer group);

	@Query(value = "SELECT * FROM lessons l WHERE l.group_id = :group_num AND l.date = :date",
			nativeQuery = true)
	List<Lesson> findLessonsByGroupAndDate(@Param("group_num") Integer groupNum,
										   @Param("date") LocalDate date);

	List<Lesson> findLessonsByTeachers(Teacher teacher);

	Integer deleteByGroupGroupNumAndDate(Integer group, LocalDate date);

	void deleteByDateAndStartTimeAndGroupGroupNum(LocalDate date, LocalTime startTime, Integer group);

	void deleteAllByGroupGroupNum(Integer groupNum);

	boolean existsByGroupGroupNum(Integer groupNum);
}
