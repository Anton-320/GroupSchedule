package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.components.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.utility.LessonUtility;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
  public GroupLessonListDto getByGroup(Integer groupNumber) {
    return LessonUtility.convertToGroupLessonListDto(
        lessonRepository.findLessonsByGroupGroupNumber(groupNumber),
        groupNumber
    );
  }

  public LessonDto getById(Long id) {
    LessonDto tmpDto = cache.get(id).orElse(null);
    if (tmpDto != null) {
      return tmpDto;
    }
    Lesson tmp = lessonRepository.findById(id).orElse(null);
    if (tmp != null) {
      tmpDto = LessonUtility.convertToLessonDto(tmp);
      cache.put(id, tmpDto);
      return tmpDto;
    }
    return null;
  }

  @Transactional
  public List<LessonDto> getByGroupAndDate(Integer groupNumber, String dateInStr) {
    LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
    return LessonUtility.convertToLessonDtoList(
        lessonRepository.findLessonsByGroupAndDate(groupNumber, date));
  }

  @Transactional
  public List<DateLessonListDto> getByTeacher(String urlId) {
    return LessonUtility.convertToDateLessonListDtoList(
        lessonRepository.findLessonsByTeachers(
            teacherRepository.findByUrlId(urlId)));
  }

  @Transactional
  public LessonDto add(LessonDto lessonDto, String dateInStr, Integer groupNumber) {
    LessonDto result;
    LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
    Lesson lesson = lessonRepository.findLessonByGroupGroupNumberAndDateAndStartTime(
        groupNumber, date, LocalTime.parse(
            lessonDto.getStartTime(),
            DateTimeFormatter.ofPattern(TIME_FORMAT))).orElse(null);
    if (lesson == null)	{	//if it doesn't exists, create new lesson entity
      lesson = createLesson(lessonDto, date, groupNumber);
      result = LessonUtility.convertToLessonDto(lesson);
    } else {
      cache.remove(lesson.getId());
      result = LessonUtility.convertToLessonDto(updateLesson(lesson, lessonDto));
    }
    cache.put(lesson.getId(), result);
    return result;
  }

  @Transactional
  public List<LessonDto> addBatch(Integer groupNumber, String dateInString, List<LessonDto> lessonDtos) {
    List<LessonDto> result = new ArrayList<>();
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    lessonDtos.stream().forEach(lessonDto -> {
      Lesson lesson = lessonRepository.findLessonByGroupGroupNumberAndDateAndStartTime(
          groupNumber, date, LocalTime.parse(
              lessonDto.getStartTime(),
              DateTimeFormatter.ofPattern(TIME_FORMAT))).orElse(null);
      LessonDto addedLessonDto;
      if (lesson == null)	{	//if it doesn't exists, create new lesson entity
        lesson = createLesson(lessonDto, date, groupNumber);
        addedLessonDto = LessonUtility.convertToLessonDto(lesson);
      } else {
        cache.remove(lesson.getId());
        addedLessonDto = LessonUtility.convertToLessonDto(updateLesson(lesson, lessonDto));
      }
      cache.put(lesson.getId(), addedLessonDto);
      result.add(addedLessonDto);
    });
    return result;
  }

  @Transactional
  public LessonDto update(Long id, LessonDto lessonDto) throws RuntimeException {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("The lesson with such an id is not found"));
    cache.remove(id);
    cache.put(id, lessonDto);
    return LessonUtility.convertToLessonDto(
        lessonRepository.save(updateLesson(lesson, lessonDto)));
  }

  @Transactional
  public boolean deleteByGroup(Integer groupNumber) {
    List<Lesson> tmp = lessonRepository.findLessonsByGroupGroupNumber(groupNumber);
    if (tmp.isEmpty()) {
      return false;
    }
    tmp.forEach(lesson -> cache.remove(lesson.getId()));
    deleteLinksOfLessons(tmp);
    lessonRepository.deleteAll(tmp);
    return true;
  }

  @Transactional
  public boolean deleteByGroupAndDate(Integer groupNumber, String dateInStr) {
    LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
    List<Lesson> tmp = lessonRepository.findLessonsByGroupAndDate(groupNumber, date);
    if (tmp.isEmpty()) {
      return false;
    }
    tmp.forEach(lesson -> cache.remove(lesson.getId()));
    deleteLinksOfLessons(tmp);
    return lessonRepository.deleteByGroupGroupNumberAndDate(groupNumber, date) != 0;
  }

  @Transactional
  public boolean deleteByGroupAndDateAndTime(String dateInStr, String startTimeInStr, Integer groupNumber) {
    LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
    LocalTime startTime = LocalTime.parse(startTimeInStr, DateTimeFormatter.ofPattern(TIME_FORMAT));
    Lesson lesson = lessonRepository.findLessonByGroupGroupNumberAndDateAndStartTime(groupNumber, date, startTime).orElse(null);
    if (lesson == null) {
      return false;
    }
    lesson.getAuditoriums().forEach(auditorium -> auditorium.getLessons().remove(lesson));
    lesson.getTeachers().forEach(teacher -> teacher.getLessons().remove(lesson));
    lesson.getAuditoriums().clear();
    lesson.getTeachers().clear();
    cache.remove(lesson.getId());
    lessonRepository.delete(lesson);
    return true;
  }

  private Lesson createLesson(LessonDto lessonDto, LocalDate date, Integer groupNumber) {
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
    addTeachersAndAuditoriums(lesson, lessonDto);
    StudentGroup tmp = groupRepository.findByGroupNumber(groupNumber);
    if (tmp == null) {
      lesson.setGroup(new StudentGroup(groupNumber));
    } else {
      lesson.setGroup(tmp);
    }
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
    addTeachersAndAuditoriums(lesson, newLessonForm);
    lessonRepository.flush();
    return lesson;
  }

  private void deleteLinksOfLessons(List<Lesson> lessons) {
    lessons.forEach(this::deleteLinksOfLesson);
  }

  private void deleteLinksOfLesson(Lesson lesson) {
    lesson.getAuditoriums().forEach(auditorium -> auditorium.getLessons().remove(lesson));
    lesson.getTeachers().forEach(teacher -> teacher.getLessons().remove(lesson));
    lesson.getAuditoriums().clear();
    lesson.getTeachers().clear();
  }

  //creates and links teachers and auditoriums to the lesson entity according to the dto content
  private void addTeachersAndAuditoriums(Lesson lesson, LessonDto lessonDto) {
    lessonDto.getAuditoriums().forEach(auditorium -> {
      Auditorium entity = auditoriumRepository.findByName(auditorium);
      if (entity == null) {
        entity = new Auditorium(auditorium);
      }
      entity.getLessons().add(lesson);
      lesson.getAuditoriums().add(entity);
    });

    lessonDto.getTeachers().forEach(tmp -> {
      Teacher teacher = teacherRepository.findByUrlId(tmp.getUrlId());
      if (teacher == null) {
        teacher = teacherRepository.findByNameAndSurnameAndPatronymic(
            tmp.getName(), tmp.getSurname(), tmp.getPatronymic());
      }
      if (teacher == null) {        //if not found, create
        teacher = TeacherUtility.createEntityObjWithoutLink(tmp);
      }
      // link teacher entity to updated lesson
      // anyway, as all the links were deleted by deleteLinksOfLesson
      teacher.getLessons().add(lesson);
      lesson.getTeachers().add(teacher);
    });
  }
}
