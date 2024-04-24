package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.service.changes.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleChangesControllerTest {

  @InjectMocks
  private ScheduleChangesController controller;

  @Mock
  private LessonService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll() {
    List<GroupLessonListDto> expected = new ArrayList<>();
    expected.add(new GroupLessonListDto(250501, new ArrayList<>()));
    when(service.getAll()).thenReturn(expected);
    List<GroupLessonListDto> result = controller.getAllScheduleChanges();
    assertEquals(expected, result);
  }

  @Test
  void getAllGroupScheduleChanges() {
    Integer groupNumber = 250501;
    GroupLessonListDto expected = new GroupLessonListDto(250501, new ArrayList<>());
    when(service.getByGroup(groupNumber)).thenReturn(expected);
    GroupLessonListDto result = controller.getAllGroupScheduleChanges(groupNumber);
    assertEquals(expected, result);
  }

  @Test
  void getById_Null() {
    Long id = 5L;
    when(service.getById(id)).thenReturn(new LessonDto());
    ResponseEntity<LessonDto> result = controller.getById(id);
    assertEquals(result.getStatusCode().value(), HttpStatus.FOUND.value());
  }

  @Test
  void getById_NotNull() {
    Long id = 5L;
    when(service.getById(id)).thenReturn(null);
    ResponseEntity<LessonDto> result = controller.getById(id);
    assertEquals(result.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
  }

  @Test
  void getGroupScheduleChangesByDate() {
    Integer groupNumber = 250501;
    String date = "05-04-2024";
    List<LessonDto> expected = new ArrayList<>();
    expected.add(new LessonDto());
    when(service.getByGroupAndDate(groupNumber, date)).thenReturn(expected);
    List<LessonDto> result = controller.getGroupScheduleChangesByDate(date, groupNumber);
    assertEquals(expected, result);
  }

  @Test
  void getScheduleChangesByTeacher() {
    String urlId = "l-podenok";
    List<DateLessonListDto> expected = new ArrayList<>();
    expected.add(new DateLessonListDto());
    when(service.getByTeacher(urlId)).thenReturn(expected);
    List<DateLessonListDto> result = controller.getScheduleChangesByTeacher(urlId);
    assertEquals(expected, result);
  }

  @Test
  void addScheduleChange() {
    LessonDto expectedValue = new LessonDto();
    String date = "05-04-2024";
    Integer groupNumber = 250501;
    when(service.add(expectedValue, date, groupNumber)).thenReturn(expectedValue);
    ResponseEntity<LessonDto> result = controller.addScheduleChange(groupNumber, date, expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertTrue(result.getStatusCode().is2xxSuccessful());
  }
  
  @Test
  void addScheduleChangesBatch() {
    List<LessonDto> expectedValue = new ArrayList<>();
    expectedValue.add(new LessonDto());
    String date = "05-04-2024";
    Integer groupNumber = 250501;
    when(service.addBatch(groupNumber, date, expectedValue)).thenReturn(expectedValue);
    ResponseEntity<List<LessonDto>> result = controller.addScheduleChanges(groupNumber, date, expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertTrue(result.getStatusCode().is2xxSuccessful());
  }

  @Test
  void updateScheduleChange() {
    LessonDto expectedValue = new LessonDto();
    Long id = 5L;
    when(service.update(id, expectedValue)).thenReturn(expectedValue);
    ResponseEntity<LessonDto> result = controller.updateScheduleChange(id, expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertTrue(result.getStatusCode().is2xxSuccessful());
  }

  @Test
  void deleteScheduleChangesByGroup_Exist() {
    Integer groupNumber = 250501;
    when(service.deleteByGroup(groupNumber)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteScheduleChanges(groupNumber);
    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertEquals("Deleting was successful", result.getBody());
  }

  @Test
  void deleteScheduleChangesByGroup_DoNotExist() {
    Integer groupNumber = 250501;
    when(service.deleteByGroup(groupNumber)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteScheduleChanges(groupNumber);
    assertTrue(result.getStatusCode().is4xxClientError());
    assertEquals("Deleting wasn't successful", result.getBody());
  }


  @Test
  void deleteScheduleChangesByGroupAndDate_Exist() {
    Integer groupNumber = 250501;
    String date = "06-05-2024";
    when(service.deleteByGroupAndDate(groupNumber, date)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteScheduleChanges(groupNumber, date);
    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertEquals("Deleting was successful", result.getBody());
  }

  @Test
  void deleteScheduleChangesByGroupAndDate_DoNotExist() {
    Integer groupNumber = 250501;
    String date = "06-05-2024";
    when(service.deleteByGroupAndDate(groupNumber, date)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteScheduleChanges(groupNumber, date);
    assertTrue(result.getStatusCode().is4xxClientError());
    assertEquals("Deleting wasn't successful", result.getBody());
  }

  @Test
  void deleteScheduleChangeByGroupAndDateAndTime_Exists() {
    Integer groupNumber = 250501;
    String date = "06-05-2024";
    String startTime = "17:10";
    when(service.deleteByGroupAndDateAndTime(date, startTime, groupNumber)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteScheduleChange(groupNumber, date, startTime);
    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertEquals("Deleting was successful", result.getBody());
  }

  @Test
  void deleteScheduleChangeByGroupAndDateAndTime_DoesNotExist() {
    Integer groupNumber = 250501;
    String date = "06-05-2024";
    String startTime = "17:10";
    when(service.deleteByGroupAndDateAndTime(date, startTime, groupNumber)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteScheduleChange(groupNumber, date, startTime);
    assertTrue(result.getStatusCode().is4xxClientError());
    assertEquals("Deleting wasn't successful", result.getBody());
  }
}
