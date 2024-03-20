package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.LessonExtendedDto;
import com.byshnev.groupschedule.model.entity.compositekey.FullName;
import com.byshnev.groupschedule.repository.DateRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.changes.utility.LessonUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {
	private LessonRepository lessonRepository;
	private GroupRepository groupRepository;
	private DateRepository dateRepository;
	private TeacherRepository teacherRepository;

	public List<LessonExtendedDto> getByGroup(Integer groupNum) {
		return LessonUtility.convertToLessonWithDateDtoList(
				lessonRepository.findLessonsByGroup(
								groupRepository.findByGroupNum(groupNum)));
	}

	public List<LessonDto> getByGroupAndDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return LessonUtility.convertToLessonDtoList(
				lessonRepository.findLessonsByGroupAndDate(
						groupRepository.findByGroupNum(groupNum),
						dateRepository.findByDateValue(date)));
	}

	public List<LessonExtendedDto> getByTeacher(String name, String surname, String patronymic) {
		return LessonUtility.convertToLessonWithDateDtoList(
				lessonRepository.findLessonsByTeachers(
						teacherRepository.findByFullname(new FullName(name, surname, patronymic))));
	}

	public LessonExtendedDto add(LessonDto lessonDto, String dateInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		Lesson lesson = LessonUtility.convertToLessonEntity(lessonDto, date, groupNum);
		return LessonUtility.convertToLessonWithDateDto(lessonRepository.save(lesson));
	}

	public LessonExtendedDto update(String dateInStr, String startTimeInStr, LessonDto lessonDto, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern("HH:mm"));
		Lesson lesson;
		if ((lesson = lessonRepository.findLessonByDateDateValueAndStartTime(date, startTime)) == null)
			return null;
		Long tmp = lesson.getId();		//save the lesson's Id
		lesson = LessonUtility.convertToLessonEntity(lessonDto, date, groupNum);
		lesson.setId(tmp);
		return LessonUtility.convertToLessonWithDateDto(lessonRepository.save(lesson));
	}

	public boolean deleteByDate(LocalDate date, Integer groupNum) {
		return lessonRepository.deleteByGroupAndDateDateValue(groupRepository.findByGroupNum(groupNum), date);
	}

	public boolean deleteByDateAndTime(LocalDate date, LocalTime time, Integer groupNum) {
		return lessonRepository.deleteByDateDateValueAndStartTimeAndGroup(date, time, groupRepository.findByGroupNum(groupNum));
	}
}
