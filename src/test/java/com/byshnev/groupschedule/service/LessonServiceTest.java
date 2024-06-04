package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.dto.*;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.changes.LessonService;
import com.byshnev.groupschedule.service.utility.LessonUtility;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

  @Mock
  private LessonRepository lessonRepository;

  @InjectMocks
  private LessonService service;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private TeacherRepository teacherRepository;

  @Mock
  private AuditoriumRepository auditoriumRepository;

  @Mock
  private ScheduleChangesCache cache;

  private static final String DATE_FORMAT = "dd-MM-yyyy";
  private static final String TIME_FORMAT = "HH:mm";

  @BeforeEach
  void setUp() {
    //initialize all the fields (marked by Mockito annotations)
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll() {
    final List<Lesson> lessons = createTestLessonList();
    when(lessonRepository.findAll()).thenReturn(lessons);
    List<GroupChangeListDto> result = service.getAll();
    assertEquals(1, result.size());
  }

  @Test
  void getByGroup() {
    Integer groupNumber = 250501;
    final List<Lesson> foundLessons = createTestLessonList();
    when(lessonRepository.findLessonsByGroupGroupNumber(groupNumber)).thenReturn(foundLessons);
    GroupChangeListDto result = service.getByGroup(groupNumber);
    verify(lessonRepository, times(1))
        .findLessonsByGroupGroupNumber(groupNumber);
    assertEquals(1, result.getLessons().size());  //1, as lessons are of one date
  }

  @Test
  void getById_ExistsInCache() {
    Long id = 3L;
    ChangeDto dto = createTestChangeDto();
    when(cache.get(id)).thenReturn(Optional.of(dto));
    ChangeDto result = service.getById(id);
    verify(cache, times(1)).get(id);
    verify(lessonRepository, never()).findById(any());
    verify(cache, never()).put(any(), any());
    assertEquals(dto, result);
  }

  @Test
  void getById_ExistsInRepository() {
    Long id = 3L;
    Lesson foundValue = createTestLesson();
    when(cache.get(id)).thenReturn(Optional.empty());
    when(lessonRepository.findById(id)).thenReturn(Optional.of(foundValue));
    service.getById(id);
    verify(cache, times(1)).get(id);
    verify(lessonRepository, times(1)).findById(id);
    verify(cache, times(1)).put(any(), any());
  }

  @Test
  void getById_DoesNotExist() {
    Long id = 3L;
    when(cache.get(id)).thenReturn(Optional.empty());
    when(lessonRepository.findById(id)).thenReturn(Optional.empty());
    ChangeDto result = service.getById(id);
    verify(cache, times(1)).get(id);
    verify(lessonRepository, times(1)).findById(id);
    verify(cache, never()).put(any(), any());
    assertNull(result);
  }

  @Test
  void getByGroupAndDate() {
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    List<Lesson> lessons = createTestLessonList();
    when(lessonRepository.findLessonsByGroupAndDate(groupNumber, date)).thenReturn(lessons);
    List<ChangeDto> result = service.getByGroupAndDate(groupNumber, dateInString);
    verify(lessonRepository, times(1))
        .findLessonsByGroupAndDate(groupNumber, date);
    assertNotNull(result);
  }

  @Test
  void getByTeacher() {
    List<Lesson> lessons = createTestLessonList();
    Teacher teacher = lessons.get(0).getTeachers().get(0);
    String urlId = teacher.getUrlId();
    when(teacherRepository.findByUrlId(urlId)).thenReturn(teacher);
    when(lessonRepository.findLessonsByTeachers(teacher)).thenReturn(lessons);
    List<DateChangeListDto> result = service.getByTeacher(urlId);
    verify(teacherRepository, times(1))
        .findByUrlId(urlId);
    verify(lessonRepository, times(1))
        .findLessonsByTeachers(teacher);
    assertNotNull(result);
  }

  @Test
  void add_Exists() {
    LessonDto dto = createTestLessonDto();
    Lesson foundLesson = createTestLesson();
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    LocalTime time = LocalTime.of(15, 50);
    when(lessonRepository.findConcreteLesson(
        groupNumber, date, time, 0)).thenReturn(Optional.of(foundLesson));
    ChangeDto result = service.add(dto, dateInString, groupNumber);
    verify(lessonRepository, times(1))
        .findConcreteLesson(groupNumber, date, time, 0);
    assertNotNull(result);
  }

  @Test
  void add_DoesNotExist() {
    Lesson savedLesson = createTestLesson();
    LessonDto dto = LessonUtility.convertToLessonDto(savedLesson);
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    LocalDate date = savedLesson.getDate();
    LocalTime time = savedLesson.getStartTime();
    when(lessonRepository.findConcreteLesson(
        groupNumber, date, time, 0)).thenReturn(Optional.empty());
    when(groupRepository.findByGroupNumber(groupNumber)).thenReturn(new StudentGroup(groupNumber));
    when(lessonRepository.save(any())).thenReturn(savedLesson);
    ChangeDto result = service.add(dto, dateInString, groupNumber);
    verify(lessonRepository, times(1))
        .findConcreteLesson(groupNumber, date, time, 0);
    verify(groupRepository, times(1)).findByGroupNumber(groupNumber);
    verify(cache, never()).remove(any());
    verify(cache, times(1)).put(any(), any());
    assertNotNull(result);
  }

  @Test
  void addBatch() {
    List<Lesson> savedLessons = createTestLessonList();
    List<LessonDto> dtoList = LessonUtility.convertToLessonDtoList(savedLessons);
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    LocalDate date = savedLessons.get(0).getDate();
    LocalTime time0 = savedLessons.get(0).getStartTime();
    LocalTime time1 = savedLessons.get(1).getStartTime();
    when(lessonRepository.findConcreteLesson(
        groupNumber, date, time0, 0)).thenReturn(Optional.of(createTestLesson()));
    when(lessonRepository.findConcreteLesson(
        groupNumber, date, time1, 0)).thenReturn(Optional.empty());
    when(groupRepository.findByGroupNumber(groupNumber)).thenReturn(new StudentGroup(groupNumber));
    when(lessonRepository.save(any())).thenReturn(savedLessons.get(1));
    List<ChangeDto> result = service.addBatch(groupNumber, dateInString, dtoList);
    verify(lessonRepository, times(2))
        .findConcreteLesson(any(), any(), any(), any());
    verify(groupRepository, times(1)).findByGroupNumber(groupNumber);
    verify(cache, times(1)).remove(any());
    verify(cache, times(2)).put(any(), any());
    assertNotNull(result);
  }

  @Test
  void update_Exists() {
    Lesson foundLesson = createTestLesson();
    LessonDto newLessonForm = createTestLessonDto();
    Long id = foundLesson.getId();
    when(lessonRepository.findById(id)).thenReturn(Optional.of(foundLesson));
    foundLesson.setName(newLessonForm.getName());
    foundLesson.setSubjectFullName(newLessonForm.getSubjectFullName());
    foundLesson.setEndTime(
        LocalTime.parse(newLessonForm.getEndTime(), DateTimeFormatter.ofPattern(TIME_FORMAT)));
    foundLesson.setNote(newLessonForm.getNote());
    foundLesson.setLessonTypeAbbr(newLessonForm.getLessonTypeAbbr());
    foundLesson.setSubgroupNum(newLessonForm.getSubgroupNum());

    foundLesson.getAuditoriums().forEach(auditorium -> auditorium.getLessons().remove(foundLesson));
    foundLesson.getTeachers().forEach(teacher -> teacher.getLessons().remove(foundLesson));
    foundLesson.getAuditoriums().clear();
    foundLesson.getTeachers().clear();

    newLessonForm.getAuditoriums().forEach(auditorium -> {
      Auditorium entity = auditoriumRepository.findByName(auditorium);
      if (entity == null) {
        entity = new Auditorium(auditorium);
      }
      entity.getLessons().add(foundLesson);
      foundLesson.getAuditoriums().add(entity);
    });

    newLessonForm.getTeachers().forEach(tmp -> {
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
      teacher.getLessons().add(foundLesson);
      foundLesson.getTeachers().add(teacher);
    });

    when(lessonRepository.save(any())).thenReturn(foundLesson);
    ChangeDto result = assertDoesNotThrow(() -> service.update(id, newLessonForm));
    verify(lessonRepository, times(1)).findById(id);
    verify(cache, times(1)).remove(id);
    verify(cache, times(1)).put(id, result);
    verify(lessonRepository, times(1)).save(any());
    assertNotNull(result);
  }

  @Test
  void update_DoesNotExist() {
    Long id = 5L;
    LessonDto dto = createTestLessonDto();
    when(lessonRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class, () -> service.update(id, dto));
  }

  @Test
  void deleteByGroup_Exist() {
    Integer groupNumber = 250501;
    List<Lesson> foundLessons = createTestLessonList();
    when(lessonRepository.findLessonsByGroupGroupNumber(groupNumber)).thenReturn(foundLessons);
    boolean returnValue = service.deleteByGroup(groupNumber);
    verify(lessonRepository, times(1)).findLessonsByGroupGroupNumber(groupNumber);
    verify(cache, times(2)).remove(any());
    verify(lessonRepository, times(1)).deleteAll(any());
    assertTrue(returnValue);
  }

  @Test
  void deleteByGroup_DoNotExist() {
    Integer groupNumber = 250501;
    when(lessonRepository.findLessonsByGroupGroupNumber(groupNumber))
        .thenReturn(Collections.emptyList());
    boolean returnValue = service.deleteByGroup(groupNumber);
    verify(lessonRepository, times(1)).findLessonsByGroupGroupNumber(groupNumber);
    verify(cache, times(0)).remove(any());
    verify(lessonRepository, times(0)).deleteAll(any());
    assertFalse(returnValue);
  }

  @Test
  void deleteByGroupAndDate_Exist() {
    Integer groupNumber = 250501;
    List<Lesson> foundLessons = createTestLessonList();
    String dateInString = "05-04-2024";
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    when(lessonRepository.findLessonsByGroupAndDate(groupNumber, date)).thenReturn(foundLessons);
    boolean returnValue = service.deleteByGroupAndDate(groupNumber, dateInString);
    verify(lessonRepository, times(1)).findLessonsByGroupAndDate(groupNumber, date);
    verify(cache, times(2)).remove(any());
    verify(lessonRepository, times(1)).deleteByGroupGroupNumberAndDate(any(), any());
    assertTrue(returnValue);
  }

  @Test
  void deleteByGroupAndDate_DoNotExist() {
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    when(lessonRepository.findLessonsByGroupAndDate(groupNumber, date))
        .thenReturn(Collections.emptyList());
    boolean returnValue = service.deleteByGroupAndDate(groupNumber, dateInString);
    verify(lessonRepository, times(1)).findLessonsByGroupAndDate(groupNumber, date);
    verify(cache, times(0)).remove(any());
    verify(lessonRepository, times(0)).deleteByGroupGroupNumberAndDate(any(), any());
    assertFalse(returnValue);
  }

  @Test
  void deleteById_Exists() {
    Long id = 0L;
    Lesson foundLesson = createTestLesson();
    when(lessonRepository.findById(id))
        .thenReturn(Optional.of(foundLesson));
    boolean returnValue = service
        .deleteById(id);
    verify(lessonRepository, times(1))
        .findById(id);
    verify(cache, times(1)).remove(any());
    verify(lessonRepository, times(1)).delete(any());
    assertTrue(returnValue);
  }

  @Test
  void deleteById_DoesNotExist() {
    Long id = 0L;
    when(lessonRepository.findById(id))
        .thenReturn(Optional.empty());
    boolean returnValue = service
        .deleteById(id);
    verify(lessonRepository, times(1))
        .findById(id);
    verify(cache, times(0)).remove(any());
    verify(lessonRepository, times(0)).delete(any());
    assertFalse(returnValue);
  }

  private List<Lesson> createTestLessonList() {
    List<Lesson> result = new ArrayList<>();

    Lesson lesson_1 = new Lesson(
        0L, null,
        LocalDate.of(2024, 4,5),
        "ОИнфБ", null,
        LocalTime.of(14,0),
        LocalTime.of(15,20),
        null, "ЛК",
        null, 0, null);

    Lesson lesson_2 = new Lesson(
        1L, null,
        LocalDate.of(2024, 4,5),
        "ОУИС", null,
        LocalTime.of(15,50), LocalTime.of(17,10),
        null, "ЛК", null, 0, null);

    lesson_1.setAuditoriums(new ArrayList<>(List.of(
        new Auditorium(0L, "311-1 к.", new ArrayList<>(List.of(lesson_1, lesson_2))))));
    lesson_1.setTeachers(new ArrayList<>(List.of(
        new Teacher("nataly", "Наталья", "Смирнова",
                    "Анатольевна", "", "zismirnova@bsuir.by",
                    new ArrayList<>(List.of(lesson_1, lesson_2))))));
    lesson_1.setGroup(new StudentGroup(
        250501, 21, new ArrayList<>(List.of(lesson_1, lesson_2))));

    lesson_2.setTeachers(new ArrayList<>(List.of(lesson_1.getTeachers().get(0))));
    lesson_2.setAuditoriums(new ArrayList<>(List.of(lesson_1.getAuditoriums().get(0))));
    lesson_2.setGroup(lesson_1.getGroup());

    result.add(lesson_1);
    result.add(lesson_2);

    return result;
  }

  private Lesson createTestLesson() {
    Lesson lesson = new Lesson(
        0L, null,
        LocalDate.of(2024, 4,5),
        "ОИнфБ", null,
        LocalTime.of(14,0),
        LocalTime.of(15,20),
        "note", "ЛК",
        null, 0, null);
    lesson.setAuditoriums(new ArrayList<>(List.of(
        new Auditorium(0L, "311-1 к.", new ArrayList<>(List.of(lesson))))));
    lesson.setTeachers(new ArrayList<>(List.of(
        new Teacher("nataly", "Наталья", "Смирнова",
                    "Анатольевна", "", "zismirnova@bsuir.by",
                    new ArrayList<>(List.of(lesson))))));
    lesson.setGroup(new StudentGroup(
        250501, 21, new ArrayList<>(List.of(lesson))));
    return lesson;
  }

  private ChangeDto createTestChangeDto() {
    return new ChangeDto(
        0L, "ОУИС", null, "15:50", "17:10",
        null, "ЛК", List.of("311-1 к."), 0,
        new ArrayList<>(List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        ))));
  }

  private LessonDto createTestLessonDto() {
    return new LessonDto(
        "ОУИС", null, "15:50", "17:10",
        null, "ЛК", List.of("311-1 к."), 0,
        new ArrayList<>(List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        ))));
  }
}

