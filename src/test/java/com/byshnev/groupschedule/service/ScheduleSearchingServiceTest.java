package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.api.BsuirApiService;
import com.byshnev.groupschedule.components.cache.ScheduleGettingCache;
import com.byshnev.groupschedule.model.dto.LessonDto;
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
import java.util.List;

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

  @Test
  void getSchedule() throws JsonProcessingException {
    List<LessonDto> result = bsuirApiService.getScheduleFromBsuirApi(
        250501, LocalDate.of(2024, 4, 5));
    
  }

}
