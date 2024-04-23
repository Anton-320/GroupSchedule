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
import static org.mockito.Mockito.when;

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
    final List<Lesson> lessons = new ArrayList<>();
    lessons.add(new Lesson(0L, new StudentGroup(250501, 21),
                           LocalDate.of(2024, 4,5),
                           "ОИнфБ", null,
                           LocalTime.of(14,0),
                           LocalTime.of(15,20),
                           null, "ЛК",
                           List.of(new Auditorium("311-1 к.")),
                           0, List.of(new Teacher(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        ))));
    lessons.add(new Lesson(
        0L, new StudentGroup(251002, 25),
        LocalDate.of(2024, 4,5),
        "ОУИС", null,
        LocalTime.of(15,50), LocalTime.of(17,10),
        null, "ЛК", List.of(new Auditorium("311-1 к.")), 0,
        List.of(new Teacher(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        ))));
    when(lessonRepository.findAll()).thenReturn(lessons);
    List<GroupLessonListDto> result = service.getAll();
    assertEquals(result.size(), 2);
  }
}