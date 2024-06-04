package com.byshnev.groupschedule.repository;

import com.byshnev.groupschedule.model.entity.Lesson;
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

  @Query(value = "SELECT * FROM lessons l WHERE l.group_id = :group_num "
      + "AND l.date = :date AND l.start_time = :start_time AND l.subgroup_num = :subgroup_num",
      nativeQuery = true)
  Optional<Lesson> findConcreteLesson(
      @Param("group_num") Integer group, @Param("date") LocalDate date,
      @Param("start_time") LocalTime startTime, @Param("subgroup_num") Integer subgroupNum);

  List<Lesson> findLessonsByGroupGroupNumber(Integer group);

  @Query(value = "SELECT * FROM lessons l WHERE l.group_id = :group_num AND l.date = :date",
      nativeQuery = true)
  List<Lesson> findLessonsByGroupAndDate(@Param("group_num") Integer groupNum,
                                         @Param("date") LocalDate date);

  List<Lesson> findLessonsByTeachers(Teacher teacher);

  Integer deleteByGroupGroupNumberAndDate(Integer group, LocalDate date);
}
