package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.service.search.ScheduleSearchingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleSearchingServiceTest {
  @InjectMocks
  private ScheduleSearchingController controller;

  @Mock
  private ScheduleSearchingService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getSchedule() throws JsonProcessingException {
    List<LessonDto> expectedValue = new ArrayList<>();
    expectedValue.add(new LessonDto(
        "ОУИС", null, "15:50", "17:10",
        null, "ЛК", List.of("311-1 к."), 0,
        new ArrayList<>(List.of(new TeacherDto(
            "nataly", "Наталья", "Смирнова",
            "Анатольевна", "", "zismirnova@bsuir.by"
        )))));
    Integer groupNumber = 250501;
    String date = "06-05-2024";
    when(service.getSchedule(groupNumber, date)).thenReturn(expectedValue);
    List<LessonDto> result = controller.getSchedule(groupNumber, date);
    assertEquals(expectedValue, result);
  }

  @Test
  void getSchedule_ThrowsException() throws JsonProcessingException {
    Integer groupNumber = 250501;
    String date = "06-05-2024";
    when(service.getSchedule(groupNumber, date)).thenThrow(JsonProcessingException.class);
    List<LessonDto> result = controller.getSchedule(groupNumber, date);
    assertEquals(0, result.size());
  }
}
