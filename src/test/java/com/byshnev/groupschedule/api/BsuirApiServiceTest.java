package com.byshnev.groupschedule.api;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BsuirApiServiceTest {

  @InjectMocks
  private BsuirApiService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @ParameterizedTest
  @ValueSource(strings = { "05-04-2024", "05-05-2024", "06-05-2024" })
  void getScheduleFromBsuirApi_NotSundayFoundLessons(String dateInString) throws JsonProcessingException {
    Integer groupNumber = 250501;
    LocalDate date = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    List<LessonDto> result = service.getScheduleFromBsuirApi(groupNumber, date);
    assertNotNull(result);
  }
}
