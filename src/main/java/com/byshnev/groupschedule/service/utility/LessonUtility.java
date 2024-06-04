package com.byshnev.groupschedule.service.utility;

import com.byshnev.groupschedule.model.dto.ChangeDto;
import com.byshnev.groupschedule.model.dto.DateChangeListDto;
import com.byshnev.groupschedule.model.dto.GroupChangeListDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.dto.LessonDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LessonUtility {

  private LessonUtility() {

  }

  public static LessonDto convertToLessonDto(Lesson lesson) {
    return new LessonDto(
        lesson.getName(),
        lesson.getSubjectFullName(),
        lesson.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        lesson.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        lesson.getNote(),
        lesson.getLessonTypeAbbr(),
        lesson.getAuditoriums().stream()
            .map(Auditorium::getName)
            .toList(),
        lesson.getSubgroupNum(),
        lesson.getTeachers().stream()
            .map(TeacherUtility::convertToDto)
            .toList()
    );
  }

  public static ChangeDto convertToChangeDto(Lesson lesson) {
    return new ChangeDto(
        lesson.getId(),
        lesson.getName(),
        lesson.getSubjectFullName(),
        lesson.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        lesson.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        lesson.getNote(),
        lesson.getLessonTypeAbbr(),
        lesson.getAuditoriums().stream()
            .map(Auditorium::getName)
            .toList(),
        lesson.getSubgroupNum(),
        lesson.getTeachers().stream()
            .map(TeacherUtility::convertToDto)
            .toList()
    );
  }

  public static List<LessonDto> convertToLessonDtoList(List<Lesson> lessons) {
    return lessons.stream().map(LessonUtility::convertToLessonDto).toList();
  }

  public static List<ChangeDto> convertToChangeDtoList(List<Lesson> lessons) {
    return lessons.stream().map(LessonUtility::convertToChangeDto).toList();
  }

  public static GroupChangeListDto convertToGroupChangeListDto(List<Lesson> lessons, Integer groupNum) {
    Map<LocalDate, List<Lesson>> lessonsByDates = lessons.stream()
        .collect(Collectors.groupingBy(Lesson::getDate));
    return new GroupChangeListDto(
        groupNum,
        lessonsByDates.entrySet().stream()
            .map(lessonList -> new GroupChangeListDto.ChangeListByDateDto(
                convertToChangeDtoList(lessonList.getValue()), lessonList.getKey()))
            .toList()
    );
  }

  public static List<GroupChangeListDto> convertToGroupLessonListDtoList(List<Lesson> lessons) {
    Map<Integer, List<Lesson>> lessonsByGroup = lessons.stream()
        .collect(Collectors.groupingBy(lesson -> lesson.getGroup().getGroupNumber()));
    return lessonsByGroup.entrySet().stream()
        .map(lessonListByGroup ->	convertToGroupChangeListDto(
            lessonListByGroup.getValue(), lessonListByGroup.getKey()))
        .toList();
  }

  public static DateChangeListDto convertToDateLessonListDto(List<Lesson> lessons, LocalDate date) {
    Map<Integer, List<Lesson>> lessonsByGroup = lessons.stream()
        .collect(Collectors.groupingBy(lesson -> lesson.getGroup().getGroupNumber()));
    return new DateChangeListDto(
        date,
        lessonsByGroup.entrySet().stream()
            .map(lessonList -> new DateChangeListDto.ChangeListByGroupDto(
                lessonList.getKey(), convertToChangeDtoList(lessonList.getValue())))
            .toList());
  }

  public static List<DateChangeListDto> convertToDateLessonListDtoList(List<Lesson> lessons) {
    Map<LocalDate, List<Lesson>> lessonsByDates = lessons.stream()
        .collect(Collectors.groupingBy(Lesson::getDate));
    return lessonsByDates.entrySet().stream()
        .map(lessonListByDate -> convertToDateLessonListDto(lessonListByDate.getValue(), lessonListByDate.getKey()))
        .toList();
  }
}
