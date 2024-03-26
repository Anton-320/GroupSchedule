package com.byshnev.groupschedule.service.utility;

import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LessonUtility {

	public static LessonDto convertToLessonDto(Lesson lesson) {
		return new LessonDto(
				lesson.getName(),
				lesson.getSubjectFullName(),
				lesson.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
				lesson.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
				lesson.getNote(),
				lesson.getLessonTypeAbbr(),
				lesson.getAuditoriums().stream()
						.map(Auditorium::getAuditorium)
						.collect(Collectors.toList()),
				lesson.getSubgroupNum(),
				lesson.getTeachers().stream()
						.map(TeacherUtility::ConvertToDto)
						.collect(Collectors.toList())
		);
	}

	public static List<LessonDto> convertToLessonDtoList(List<Lesson> lessons) {
		return lessons.stream().map(LessonUtility::convertToLessonDto).collect(Collectors.toList());
	}

	public static GroupLessonListDto convertToGroupLessonListDto(List<Lesson> lessons, Integer groupNum) {
		Map<LocalDate, List<Lesson>> lessonsByDates = lessons.stream()
				.collect(Collectors.groupingBy(Lesson::getDate));
		return new GroupLessonListDto(
				groupNum,
				lessonsByDates.entrySet().stream()
						.map(lessonList -> new GroupLessonListDto.LessonListByDateDto(
								convertToLessonDtoList(lessonList.getValue()), lessonList.getKey()))
						.collect(Collectors.toList()
				)
		);
	}

	public static List<GroupLessonListDto> convertToGroupLessonListDtoList(List<Lesson> lessons) {
		Map<Integer, List<Lesson>> lessonsByGroup = lessons.stream()
				.collect(Collectors.groupingBy(lesson -> lesson.getGroup().getGroupNum()));
		return lessonsByGroup.entrySet().stream()
				.map(lessonListByGroup ->	convertToGroupLessonListDto(
						lessonListByGroup.getValue(), lessonListByGroup.getKey()))
				.collect(Collectors.toList());
	}

	public static DateLessonListDto convertToDateLessonListDto(List<Lesson> lessons, LocalDate date) {
		Map<Integer, List<Lesson>> lessonsByGroup = lessons.stream()
				.collect(Collectors.groupingBy(lesson->lesson.getGroup().getGroupNum()));
		return new DateLessonListDto(
				date,
				lessonsByGroup.entrySet().stream()
						.map(lessonList -> new DateLessonListDto.LessonListByGroupDto(
								lessonList.getKey(), convertToLessonDtoList(lessonList.getValue())))
						.collect(Collectors.toList()));
	}

	public static List<DateLessonListDto> convertToDateLessonListDtoList(List<Lesson> lessons) {
		Map<LocalDate, List<Lesson>> lessonsByDates = lessons.stream()
				.collect(Collectors.groupingBy(Lesson::getDate));
		return lessonsByDates.entrySet().stream()
				.map(lessonListByDate -> convertToDateLessonListDto(lessonListByDate.getValue(), lessonListByDate.getKey()))
				.collect(Collectors.toList());
	}
}
