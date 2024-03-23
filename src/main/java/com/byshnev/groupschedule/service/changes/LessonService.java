package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.model.entity.compositekey.FullName;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.utility.LessonUtility;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService {
	private LessonRepository lessonRepository;
	private GroupRepository groupRepository;
	private TeacherRepository teacherRepository;
	private AuditoriumRepository auditoriumRepository;

	public List<GroupLessonListDto> getAll() {
		return LessonUtility.convertToGroupLessonListDtoList(lessonRepository.findAll());
	}

	public GroupLessonListDto getByGroup(Integer groupNum) {
		return LessonUtility.convertToGroupLessonListDto(
				lessonRepository.findLessonsByGroup(groupRepository.findByGroupNum(groupNum)),
				groupNum
		);
	}

	public DateLessonListDto getByDate(String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return LessonUtility.convertToDateLessonListDto(
				lessonRepository.findByDate(date), date);
	}

	public List<LessonDto> getByGroupAndDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return LessonUtility.convertToLessonDtoList(
				lessonRepository.findLessonsByGroupAndDate(groupRepository.findByGroupNum(groupNum), date));
	}

	public List<DateLessonListDto> getByTeacher(String name, String surname, String patronymic) {
		return LessonUtility.convertToDateLessonListDtoList(
				lessonRepository.findLessonsByTeachers(
						teacherRepository.findByNameAndSurnameAndPatronymic(name, surname, patronymic)));
	}

	public LessonDto add(LessonDto lessonDto, String dateInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		Lesson lesson = lessonRepository.findLessonByGroupAndDateAndStartTime(		//check, if there exists change for thesee group, date and time
				groupRepository.findByGroupNum(groupNum), date, LocalTime.parse(
						lessonDto.getStartTime(),
						DateTimeFormatter.ofPattern("HH:mm")));
		if (lesson == null) {			//if it doesn't exists, create new lesson entity

			lesson = new Lesson(
					null,
					lessonDto.getName(),
					lessonDto.getSubjectFullName(),
					date,
					LocalTime.parse(
							lessonDto.getStartTime(),
							DateTimeFormatter.ofPattern("HH:mm")),
					LocalTime.parse(
							lessonDto.getEndTime(),
							DateTimeFormatter.ofPattern("HH:mm")),
					lessonDto.getNote(),
					lessonDto.getLessonTypeAbbr(),
					null,
					lessonDto.getSubgroupNum(),
					null);
			lesson.setAuditoriums(initCreatedLessonAuditoriums(lessonDto, lesson));
			lesson.setTeachers(initCreatedLessonTeachers(lessonDto, lesson));
			StudentGroup tmp = groupRepository.findByGroupNum(groupNum);
			if (tmp == null)
				lesson.setGroup(new StudentGroup(groupNum));
			else lesson.setGroup(tmp);

		}
		else {
			lesson = updateLesson(lesson, lessonDto);
		}
		return LessonUtility.convertToLessonDto(lessonRepository.save(lesson));
	}

	public LessonDto update(String dateInStr, String startTimeInStr, LessonDto lessonDto, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern("HH:mm"));
		Lesson lesson;
		if ((lesson = lessonRepository.findLessonByGroupAndDateAndStartTime(
				groupRepository.findByGroupNum(groupNum), date, startTime)) == null)
			return null;
		lesson = updateLesson(lesson, lessonDto);
		return LessonUtility.convertToLessonDto(lessonRepository.save(lesson));
	}

	public boolean deleteByGroup(Integer groupNum) {
		return lessonRepository.deleteByGroupGroupNum(groupNum);
	}

	public boolean deleteByGroupAndDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		return lessonRepository.deleteByGroupAndDate(groupRepository.findByGroupNum(groupNum), date);
	}

	public boolean deleteByDateAndTime(String dateInStr, String startTimeInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern("HH:mm"));
		return lessonRepository.deleteByDateAndStartTimeAndGroup(date, startTime, groupRepository.findByGroupNum(groupNum));
	}

	private Lesson updateLesson(Lesson lesson, LessonDto newLessonForm) {
			lesson.setName(newLessonForm.getName());
			lesson.setSubjectFullName(newLessonForm.getSubjectFullName());
			lesson.setEndTime(LocalTime.parse(newLessonForm.getEndTime(), DateTimeFormatter.ofPattern("HH:mm")));
			lesson.setNote(newLessonForm.getNote());
			lesson.setLessonTypeAbbr(newLessonForm.getLessonTypeAbbr());
			lesson.setAuditoriums(initUpdatedLessonAuditoriums(newLessonForm, lesson));
			lesson.setSubgroupNum(newLessonForm.getSubgroupNum());
			lesson.setTeachers(initUpdatedLessonTeachers(newLessonForm, lesson));
			return lesson;
	}

	private List<Auditorium> initUpdatedLessonAuditoriums(LessonDto lessonDto, Lesson lesson) {
		return lessonDto.getAuditoriums().stream()
				.map((auditorium) -> {

					Auditorium tmp = auditoriumRepository.findByAuditorium(auditorium);
					if (tmp == null) {
						tmp = new Auditorium(auditorium);
						tmp.getLessons().add(lesson);
					}
					return tmp;

				})
				.collect(Collectors.toList());
	}

	private List<Teacher> initUpdatedLessonTeachers(LessonDto lessonDto, Lesson lesson) {
		return lessonDto.getTeachers().stream()
				.map(teacherDto -> {

					Teacher teacher = teacherRepository.findByNameAndSurnameAndPatronymic(
							teacherDto.getName(),
							teacherDto.getSurname(),
							teacherDto.getPatronymic());
					if (teacher == null) {		//if not found
						teacher = TeacherUtility.createTeacherEntityWithoutLink(teacherDto);    //create a teacherentity on the base of dto
						// link teacher entity to created lesson
						// (not anyway, as if the teacher exists, than don't neen connect existing teacher to existing lesson)
						// they are already connected by id (in the lesson_teacher table)
						teacher.getLessons().add(lesson);
					}
					return teacher;

				})
				.collect(Collectors.toList());
	}

	private List<Auditorium> initCreatedLessonAuditoriums(LessonDto lessonDto, Lesson lesson) {
		return lessonDto.getAuditoriums().stream()
				.map((auditorium) -> {

					Auditorium tmp = auditoriumRepository.findByAuditorium(auditorium);
					if (tmp == null) {
						tmp = new Auditorium(auditorium);
					}
					tmp.getLessons().add(lesson);
					return tmp;

				})
				.collect(Collectors.toList());
	}

	private List<Teacher> initCreatedLessonTeachers(LessonDto lessonDto, Lesson lesson) {
		return lessonDto.getTeachers().stream()
				.map(teacherDto -> {

					Teacher teacher = teacherRepository.findByNameAndSurnameAndPatronymic(
							teacherDto.getName(), teacherDto.getSurname(), teacherDto.getPatronymic());
					if (teacher == null) {		//if not found
						teacher = TeacherUtility.createTeacherEntityWithoutLink(teacherDto);    //create a teacherentity on the base of dto
					}
					// link teacher entity to created lesson
					// (anyway because lesson has new id,
					// therefore the table "lessons_teachers" anyway doesn't have such a link through their id)
					teacher.getLessons().add(lesson);
					return teacher;

				})
				.collect(Collectors.toList());
	}
}

