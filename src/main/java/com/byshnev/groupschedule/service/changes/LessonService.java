package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.utility.LessonUtility;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import jakarta.transaction.Transactional;
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
	private TeacherRepository teacherRepository;
	private AuditoriumRepository auditoriumRepository;
	private ScheduleChangesCache cache;

	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String TIME_FORMAT = "HH:mm";

	@Transactional
	public List<GroupLessonListDto> getAll() {
		return LessonUtility.convertToGroupLessonListDtoList(lessonRepository.findAll());
	}

	@Transactional
	public GroupLessonListDto getByGroup(Integer groupNum) {
		return LessonUtility.convertToGroupLessonListDto(
				lessonRepository.findLessonsByGroupGroupNum(groupNum),
				groupNum
		);
	}

	@Transactional
	public LessonDto getById(Long id) {
		LessonDto tmpDto = cache.get(id).orElse(null);
		if (tmpDto != null)
			return tmpDto;
		Lesson tmp = lessonRepository.findById(id).orElse(null);
		if (tmp != null) {
			tmpDto = LessonUtility.convertToLessonDto(tmp);
			cache.put(id, tmpDto);
			return tmpDto;
		}
		return null;
	}

	@Transactional
	public List<LessonDto> getByGroupAndDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
		return LessonUtility.convertToLessonDtoList(
				lessonRepository.findLessonsByGroupAndDate(groupNum, date));
	}

	@Transactional
	public List<DateLessonListDto> getByTeacher(String urlId) {
		return LessonUtility.convertToDateLessonListDtoList(
				lessonRepository.findLessonsByTeachers(
						teacherRepository.findByUrlId(urlId)));
	}

	@Transactional
	public LessonDto add(LessonDto lessonDto, String dateInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
		Lesson lesson = lessonRepository.findLessonByGroupGroupNumAndDateAndStartTime(
				groupNum, date, LocalTime.parse(
						lessonDto.getStartTime(),
						DateTimeFormatter.ofPattern(TIME_FORMAT))).orElse(null);
		LessonDto result;
		if (lesson == null)		//if it doesn't exists, create new lesson entity
			result = LessonUtility.convertToLessonDto(createLesson(lessonDto, date, groupNum));
		else
			result = LessonUtility.convertToLessonDto(updateLesson(lesson, lessonDto));
		return result;
	}

	@Transactional
	public LessonDto update(Long id, LessonDto lessonDto) throws RuntimeException {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("The lesson with such an id is not found"));
		return LessonUtility.convertToLessonDto(
				lessonRepository.save(updateLesson(lesson, lessonDto)));
	}

	@Transactional
	public boolean deleteByGroup(Integer groupNum) {
		List<Lesson> tmp = lessonRepository.findLessonsByGroupGroupNum(groupNum);
		if (tmp.isEmpty())
			return false;
		deleteLinksOfLessons(tmp);
		lessonRepository.deleteAll(tmp);
		return true;
	}

	@Transactional
	public boolean deleteByGroupAndDate(Integer groupNum, String dateInStr) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
		List<Lesson> tmp = lessonRepository.findLessonsByGroupAndDate(groupNum, date);
		if (tmp.isEmpty())
			return false;
		deleteLinksOfLessons(tmp);
		return lessonRepository.deleteByGroupGroupNumAndDate(groupNum, date) != 0;
	}

	@Transactional
	public boolean deleteByGroupAndDateAndTime(String dateInStr, String startTimeInStr, Integer groupNum) {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
		LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern(TIME_FORMAT));
		Lesson lesson = lessonRepository.findLessonByGroupGroupNumAndDateAndStartTime(groupNum, date, startTime).orElse(null);
		if (lesson == null)
			return false;
		lesson.getAuditoriums().forEach(auditorium -> auditorium.getLessons().remove(lesson));
		lesson.getTeachers().forEach(teacher -> teacher.getLessons().remove(lesson));
		lesson.getAuditoriums().clear();
		lesson.getTeachers().clear();
		lessonRepository.delete(lesson);
		return true;
	}

	private Lesson createLesson(LessonDto lessonDto, LocalDate date, Integer groupNum) {
		Lesson lesson = new Lesson(
				lessonDto.getName(),
				lessonDto.getSubjectFullName(),
				date,
				lessonDto.getNote(),
				lessonDto.getLessonTypeAbbr(),
				lessonDto.getSubgroupNum());
		lesson.setStartTime(LocalTime.parse(
				lessonDto.getStartTime(),
				DateTimeFormatter.ofPattern(TIME_FORMAT)));
		lesson.setEndTime(LocalTime.parse(
				lessonDto.getEndTime(),
				DateTimeFormatter.ofPattern(TIME_FORMAT)));

		lessonDto.getAuditoriums().forEach((auditorium) -> {
			Auditorium auditorEntity = auditoriumRepository.findByName(auditorium);
			if (auditorEntity == null) {
				auditorEntity = new Auditorium(auditorium);
			}
			//link lesson to auditorium
			auditorEntity.getLessons().add(lesson);
			lesson.getAuditoriums().add(auditorEntity);
		});

		lessonDto.getTeachers().forEach(tmp -> {
			Teacher teacher = teacherRepository.findByUrlId(tmp.getUrlId());
			if (teacher == null)
				teacher = teacherRepository.findByNameAndSurnameAndPatronymic(
						tmp.getName(), tmp.getSurname(), tmp.getPatronymic());
			if (teacher == null) {        //if not found, create
				teacher = TeacherUtility.createEntityObjWithoutLink(tmp);
			}
			// link teacher entity to updated lesson
			teacher.getLessons().add(lesson);
			lesson.getTeachers().add(teacher);
		});
		StudentGroup tmp = groupRepository.findByGroupNum(groupNum);
		if (tmp == null)
			lesson.setGroup(new StudentGroup(groupNum));
		else lesson.setGroup(tmp);
		return lessonRepository.save(lesson);
	}

	private Lesson updateLesson(Lesson lesson, LessonDto newLessonForm) {
			lesson.setName(newLessonForm.getName());
			lesson.setSubjectFullName(newLessonForm.getSubjectFullName());
			lesson.setEndTime(LocalTime.parse(newLessonForm.getEndTime(), DateTimeFormatter.ofPattern(TIME_FORMAT)));
			lesson.setNote(newLessonForm.getNote());
			lesson.setLessonTypeAbbr(newLessonForm.getLessonTypeAbbr());
			lesson.setSubgroupNum(newLessonForm.getSubgroupNum());
			deleteLinksOfLesson(lesson);	//delete lesson links to teachers and auditoriums
			lessonRepository.flush();

			newLessonForm.getAuditoriums().forEach((auditorium) -> {
				Auditorium auditorEntity = auditoriumRepository.findByName(auditorium);
				if (auditorEntity == null) {
					auditorEntity = new Auditorium(auditorium);
				}
				auditorEntity.getLessons().add(lesson);
				lesson.getAuditoriums().add(auditorEntity);
				auditoriumRepository.saveAndFlush(auditorEntity); //safe if the auditorium doesn't exist
				});

			newLessonForm.getTeachers().forEach(tmp -> {
				Teacher teacher = teacherRepository.findByUrlId(tmp.getUrlId());
				if (teacher == null)
					teacher = teacherRepository.findByNameAndSurnameAndPatronymic(
							tmp.getName(), tmp.getSurname(), tmp.getPatronymic());
				if (teacher == null) {        //if not found, create
					teacher = TeacherUtility.createEntityObjWithoutLink(tmp);
				}
				// link teacher entity to updated lesson
				// anyway, as all the links were deleted by deleteLinksOfLesson
				teacher.getLessons().add(lesson);
				lesson.getTeachers().add(teacher);
				teacherRepository.save(teacher);
			});

			return lesson;
	}

	private void deleteLinksOfLessons(List<Lesson> lessons) {
		lessons.forEach(lesson -> {
			lesson.getAuditoriums().forEach(auditorium -> auditorium.getLessons().remove(lesson));
			lesson.getTeachers().forEach(teacher -> teacher.getLessons().remove(lesson));
			lesson.getAuditoriums().clear();
			lesson.getTeachers().clear();
		});
	}

	private void deleteLinksOfLesson(Lesson lesson) {
		lesson.getAuditoriums().forEach(auditorium -> auditorium.getLessons().remove(lesson));
		lesson.getTeachers().forEach(teacher -> teacher.getLessons().remove(lesson));
		lesson.getAuditoriums().clear();
		lesson.getTeachers().clear();
	}
}

