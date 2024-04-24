package com.byshnev.groupschedule.api;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class BsuirApiServiceTest {

  @InjectMocks
  private BsuirApiService service;

  private static final String HTTPS_URL_BSUIR_SRCH = "https://iis.bsuir.by/api/v1/schedule?studentGroup={groupNumber}";
  private static final String[] DAYS_OF_WEEK = {
      "Понедельник",
      "Вторник",
      "Среда",
      "Четверг",
      "Пятница",
      "Суббота"
  };

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getScheduleFromBsuirApi_NotSundayFoundLessons() throws JsonProcessingException {
    Integer groupNumber = 250501;
    //friday
    LocalDate date = LocalDate.of(2024, 4, 5);
    List<LessonDto> result = service.getScheduleFromBsuirApi(groupNumber, date);
    assertNotNull(result);
  }

  @Test
  void getScheduleFromBsuirApi_Sunday() throws JsonProcessingException {
    Integer groupNumber = 250501;
    //sunday
    LocalDate date = LocalDate.of(2024, 5, 5);
    List<LessonDto> result = service.getScheduleFromBsuirApi(groupNumber, date);
    assertNotNull(result);
  }

  @Test
  void getScheduleFromBsuirApi_NotSundayNotFoundLessons() throws JsonProcessingException {
    Integer groupNumber = 250501;
    //not a sunday, but when there is no lessons (monday)
    LocalDate date = LocalDate.of(2024, 5, 6);
    List<LessonDto> result = service.getScheduleFromBsuirApi(groupNumber, date);
    assertNotNull(result);
  }
}
