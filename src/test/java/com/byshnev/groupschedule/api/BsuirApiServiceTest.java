package com.byshnev.groupschedule.api;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
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
  void getScheduleFromBsuirApi_NotSunday() throws JsonProcessingException {
    Integer groupNumber = 250501;
    LocalDate date = LocalDate.of(2024, 4, 5);
    List<LessonDto> result = service.getScheduleFromBsuirApi(groupNumber, date);
    assertNotNull(result);
  }

  @Test
  void getScheduleFromBsuirApi_Sunday() throws JsonProcessingException {
    Integer groupNumber = 250501;
    LocalDate date = LocalDate.of(2024, 5, 5);
    List<LessonDto> result = service.getScheduleFromBsuirApi(groupNumber, date);
    assertNotNull(result);
  }

  private List<LessonDto> createLessonDtoList() {
    return new ArrayList<>(List.of(
        new LessonDto(
            "ОСиСП",
            "Операционные системы и системное программирование",
            "09:00",
            "10:20",
            "только 09.02",
            "ЛК",
            new ArrayList<>(List.of("206-3 к.")),
            0,
            new ArrayList<>(List.of(new TeacherDto(
                "l-podenok", "Леонид", "Поденок",
                "Петрович", "", null)))),
        new LessonDto(
            "ОИнфБ",
            "Основы информационной безопасности",
            "10:35",
            "11:55",
            null,
            "ЛК",
            new ArrayList<>(List.of("431-1 к.")),
            0,
            new ArrayList<>(List.of(
                new TeacherDto(
                    "n-smirnova", "Наталия", "Смирнова",
                    "Анатольевна", "", "zismirnova@bsuir.by"))))
    ));
  }
}
