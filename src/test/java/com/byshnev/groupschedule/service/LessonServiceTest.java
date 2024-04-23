package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.changes.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

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

  @BeforeEach
  void setUp() {
    //initialize all the fields (marked by Mockito annotations)
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll() {
    final List<Lesson> lessons = createTestLessonList();
    when(lessonRepository.findAll()).thenReturn(lessons);
    List<GroupLessonListDto> result = service.getAll();
    assertEquals(1, result.size());
  }

  @Test
  void getByGroup() {
    Integer groupNumber = 250501;
    final List<Lesson> foundLessons = createTestLessonList();
    when(lessonRepository.findLessonsByGroupGroupNumber(groupNumber)).thenReturn(foundLessons);
    GroupLessonListDto result = service.getByGroup(groupNumber);
    verify(lessonRepository, times(1))
        .findLessonsByGroupGroupNumber(groupNumber);
    assertEquals(1, result.getLessons().size());  //1, as lessons are of one date
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

