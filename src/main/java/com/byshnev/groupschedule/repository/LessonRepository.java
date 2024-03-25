package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
	List<Lesson> findByDate(LocalDate date);

	Lesson findLessonByGroupAndDateAndStartTime(StudentGroup group, LocalDate date, LocalTime startTime);

	boolean deleteByGroupAndDate(StudentGroup group, LocalDate date);

	boolean deleteByDateAndStartTimeAndGroup(LocalDate date_date, LocalTime startTime, StudentGroup group);

	boolean deleteByGroupGroupNum(Integer groupNum);

	List<Lesson> findLessonsByGroup(StudentGroup group);

	List<Lesson> findLessonsByGroupAndDate(StudentGroup group, LocalDate date);

	List<Lesson> findLessonsByTeachers(Teacher teacher);
}
