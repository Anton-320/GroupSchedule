package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.changes.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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


}