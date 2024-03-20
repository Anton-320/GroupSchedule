package com.byshnev.groupschedule.service.changes.utility;

import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.LessonExtendedDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class LessonUtility {
	public static Lesson convertToLessonEntity(LessonDto lessonDto, LocalDate date, Integer group) {

		return new Lesson(group, lessonDto.getName(),
						  lessonDto.getFullName(),
						  date,
						  LocalTime.parse(
								  lessonDto.getStartTime(),
								  DateTimeFormatter.ofPattern("HH:mm")),
						  LocalTime.parse(
								  lessonDto.getEndTime(),
								  DateTimeFormatter.ofPattern("HH:mm")),
						  lessonDto.getNote(),
						  lessonDto.getLessonTypeAbbr(),
						  lessonDto.getAuditoriums().stream()
								  .map(Auditorium::new).collect(Collectors.toList()),
						  lessonDto.getSubgroupNum(),
						  lessonDto.getTeachers().stream()
								  .map(Teacher::new).collect(Collectors.toList()));
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

	public static LessonExtendedDto convertToLessonWithDateDto(Lesson lesson) {
		return new LessonExtendedDto(lesson.getGroup().getGroupNum(),
									 lesson.getDate().getDateValue(),
									 convertToLessonDto(lesson));
	}

	public static List<LessonExtendedDto> convertToLessonWithDateDtoList(List<Lesson> lessons) {
		return lessons.stream().map(LessonUtility::convertToLessonWithDateDto).collect(Collectors.toList());
	}
}
