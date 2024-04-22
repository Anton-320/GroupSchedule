package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.api.BsuirApiService;
import com.byshnev.groupschedule.components.cache.ScheduleGettingCache;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.service.search.ScheduleSearchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduleSearchingServiceTest {

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



}
