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

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
	List<Lesson> findByDate(LocalDate date);

	Lesson findLessonByGroupAndDateAndStartTime(StudentGroup group, LocalDate date, LocalTime startTime);

	boolean deleteByGroupAndDate(StudentGroup group, LocalDate date);

	boolean deleteByDateAndStartTimeAndGroup(LocalDate date, LocalTime startTime, StudentGroup group);

	boolean deleteByGroupGroupNum(Integer groupNum);


	List<Lesson> findLessonsByGroupGroupNum(Integer group);

	@Query(value = "WITH group_id AS (" +
			"SELECT id FROM student_group sg " +
				"WHERE sg.group_num = :group_num" +
			")" +
			"SELECT * FROM lessons l WHERE l.group_id = group_id AND l.date = :date",
			nativeQuery = true)
	List<Lesson> findLessonsByGroupAndDate(@Param("group_num") Integer groupNum,
										   @Param("date") LocalDate date);

	List<Lesson> findLessonsByTeachers(Teacher teacher);
}
