package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.dto.LessonDto;
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

	public GroupLessonListDto getByGroup(Integer groupNum) {
		return LessonUtility.convertToGroupLessonListDto(
				lessonRepository.findLessonsByGroup(groupRepository.findByGroupNum(groupNum)),
				groupNum
		);
	}

	public DateLessonListDto getByDate(String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return LessonUtility.convertToDateLessonListDto(
				lessonRepository.findByDate(dateRepository.findByDateValue(date)), date);
	}

	public List<LessonDto> getByGroupAndDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return LessonUtility.convertToLessonDtoList(
				lessonRepository.findLessonsByGroupAndDate(
						groupRepository.findByGroupNum(groupNum),
						dateRepository.findByDateValue(date)));
	}

	public List<DateLessonListDto> getByTeacher(String name, String surname, String patronymic) {
		return LessonUtility.convertToDateLessonListDtoList(
				lessonRepository.findLessonsByTeachers(
						teacherRepository.findByFullname(new FullName(name, surname, patronymic))));
	}

	public LessonDto add(LessonDto lessonDto, String dateInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		Lesson lesson = LessonUtility.convertToLessonEntity(lessonDto, date, groupNum);
		return LessonUtility.convertToLessonDto(lessonRepository.save(lesson));
	}

	public LessonDto update(String dateInStr, String startTimeInStr, LessonDto lessonDto, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern("HH:mm"));
		Lesson lesson;
		if ((lesson = lessonRepository.findLessonByDateDateValueAndStartTime(date, startTime)) == null)
			return null;
		Long tmp = lesson.getId();		//save the lesson's Id
		lesson = LessonUtility.convertToLessonEntity(lessonDto, date, groupNum);
		lesson.setId(tmp);
		return LessonUtility.convertToLessonDto(lessonRepository.save(lesson));
	}

	public boolean deleteByDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return lessonRepository.deleteByGroupAndDateDateValue(groupRepository.findByGroupNum(groupNum), date);
	}

	public boolean deleteByDateAndTime(String dateInStr, String startTimeInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern("HH:mm"));
		return lessonRepository.deleteByDateDateValueAndStartTimeAndGroup(date, startTime, groupRepository.findByGroupNum(groupNum));
	}
}
