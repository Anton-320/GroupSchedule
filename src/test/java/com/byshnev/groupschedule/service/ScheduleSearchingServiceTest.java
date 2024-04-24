package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.api.BsuirApiService;
import com.byshnev.groupschedule.components.cache.ScheduleGettingCache;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.service.search.ScheduleSearchingService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleSearchingServiceTest {

  @Mock
  private LessonRepository lessonRepository;

  @Mock
  private BsuirApiService bsuirApiService;

  @Mock
  private ScheduleGettingCache cache;

  @InjectMocks
  private ScheduleSearchingService service;

  @BeforeEach
  void setUp() {
    //initialize all the fields (marked by Mockito annotations)
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getSchedule_DoesNotExistInCacheWithNoChanges() throws JsonProcessingException {
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    String key = groupNumber.toString() + dateInString;
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    final List<LessonDto> scheduleWithNoChanges = new ArrayList<>();
    scheduleWithNoChanges.add(new LessonDto(
        "ОИнфБ", null, "14:00", "15:20",
        null, "ЛК", List.of("311-1 к."), 0,
        List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        ))));
    scheduleWithNoChanges.add(new LessonDto(
        "ОУИС", null, "15:50", "17:10",
        null, "ЛК", List.of("311-1 к."), 0,
        List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        ))));
    when(cache.get(key)).thenReturn(Optional.empty());
    when(bsuirApiService.getScheduleFromBsuirApi(groupNumber, date))
        .thenReturn(scheduleWithNoChanges);
    when(lessonRepository.findLessonsByGroupAndDate(groupNumber, date)).thenReturn(new ArrayList<>());
    List<LessonDto> result = service.getSchedule(groupNumber, dateInString);
    verify(cache, times(1)).get(key);
    assertEquals(scheduleWithNoChanges.size(), result.size());
  }

  @Test
  void getSchedule_DoesNotExistInCacheWithChanges() throws JsonProcessingException {
    Integer groupNumber = 250501;
    String dateInString = "05-04-2024";
    String key = groupNumber.toString() + dateInString;
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    List<LessonDto> schedule = new ArrayList<>();
    schedule.add(createTestLessonDto_1());
    schedule.add(createTestLessonDto_2());
    List<Lesson> changes = createTestLessonList();
    when(cache.get(key)).thenReturn(Optional.empty());
    when(bsuirApiService.getScheduleFromBsuirApi(groupNumber, date))
        .thenReturn(schedule);
    when(lessonRepository.findLessonsByGroupAndDate(groupNumber, date)).thenReturn(changes);
    List<LessonDto> result = service.getSchedule(groupNumber, dateInString);
    verify(cache, times(1)).get(key);
    verify(bsuirApiService, times(1))
        .getScheduleFromBsuirApi(groupNumber, date);
    verify(lessonRepository, times(1))
        .findLessonsByGroupAndDate(groupNumber, date);

    assertNotNull(result);
  }

  private LessonDto createTestLessonDto_1() {
    return new LessonDto(
        "ОИнфБ", null, "14:00", "15:20",
        null, "ЛК", List.of("311-1 к."), 0,
        List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        )));
  }

  private LessonDto createTestLessonDto_2() {
    return new LessonDto(
        "ОУИС", null, "15:50", "17:10",
        null, "ЛК", List.of("311-1 к."), 0,
        List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        )));
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

    lesson_1.setAuditoriums(List.of(
        new Auditorium(0L, "311-1 к.", List.of(lesson_1, lesson_2))));
    lesson_1.setTeachers(List.of(
        new Teacher("nataly", "Наталья", "Смирнова",
                    "Анатольевна", "", "zismirnova@bsuir.by",
                    List.of(lesson_1, lesson_2))));
    lesson_1.setGroup(new StudentGroup(
        250501, 21, List.of(lesson_1, lesson_2)));

    lesson_2.setTeachers(List.of(lesson_1.getTeachers().get(0)));
    lesson_2.setAuditoriums(List.of(lesson_1.getAuditoriums().get(0)));
    lesson_2.setGroup(lesson_1.getGroup());

    result.add(lesson_1);
    result.add(lesson_2);

    return result;
  }

}
