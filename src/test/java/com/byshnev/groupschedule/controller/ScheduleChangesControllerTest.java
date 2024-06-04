package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.ChangeDto;
import com.byshnev.groupschedule.model.dto.DateChangeListDto;
import com.byshnev.groupschedule.model.dto.GroupChangeListDto;
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
    List<GroupChangeListDto> expected = new ArrayList<>();
    expected.add(new GroupChangeListDto(250501, new ArrayList<>()));
    when(service.getAll()).thenReturn(expected);
    List<GroupChangeListDto> result = controller.getAllScheduleChanges();
    assertEquals(expected, result);
  }

  @Test
  void getAllGroupScheduleChanges() {
    Integer groupNumber = 250501;
    GroupChangeListDto expected = new GroupChangeListDto(250501, new ArrayList<>());
    when(service.getByGroup(groupNumber)).thenReturn(expected);
    GroupChangeListDto result = controller.getAllGroupScheduleChanges(groupNumber);
    assertEquals(expected, result);
  }

  @Test
  void getById_Null() {
    Long id = 5L;
    when(service.getById(id)).thenReturn(new ChangeDto());
    ResponseEntity<ChangeDto> result = controller.getById(id);
    assertEquals(result.getStatusCode().value(), HttpStatus.FOUND.value());
  }

  @Test
  void getById_NotNull() {
    Long id = 5L;
    when(service.getById(id)).thenReturn(null);
    ResponseEntity<ChangeDto> result = controller.getById(id);
    assertEquals(result.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
  }

  @Test
  void getGroupScheduleChangesByDate() {
    Integer groupNumber = 250501;
    String date = "05-04-2024";
    List<ChangeDto> expected = new ArrayList<>();
    expected.add(new ChangeDto());
    when(service.getByGroupAndDate(groupNumber, date)).thenReturn(expected);
    List<ChangeDto> result = controller.getGroupScheduleChangesByDate(date, groupNumber);
    assertEquals(expected, result);
  }

  @Test
  void getScheduleChangesByTeacher() {
    String urlId = "l-podenok";
    List<DateChangeListDto> expected = new ArrayList<>();
    expected.add(new DateChangeListDto());
    when(service.getByTeacher(urlId)).thenReturn(expected);
    List<DateChangeListDto> result = controller.getScheduleChangesByTeacher(urlId);
    assertEquals(expected, result);
  }

  @Test
  void addScheduleChange() {
    LessonDto inputValue = new LessonDto();
    ChangeDto expectedValue = new ChangeDto();
    String date = "05-04-2024";
    Integer groupNumber = 250501;
    when(service.add(inputValue, date, groupNumber)).thenReturn(expectedValue);
    ResponseEntity<ChangeDto> result = controller.addScheduleChange(groupNumber, date, inputValue);
    assertEquals(expectedValue, result.getBody());
    assertTrue(result.getStatusCode().is2xxSuccessful());
  }
  
  @Test
  void addScheduleChangesBatch() {
    List<LessonDto> inputValue = new ArrayList<>();
    inputValue.add(new LessonDto());
    List<ChangeDto> expectedValue = new ArrayList<>();
    expectedValue.add(new ChangeDto());
    String date = "05-04-2024";
    Integer groupNumber = 250501;
    when(service.addBatch(groupNumber, date, inputValue)).thenReturn(expectedValue);
    ResponseEntity<List<ChangeDto>> result = controller.addScheduleChanges(groupNumber, date, inputValue);
    assertEquals(expectedValue, result.getBody());
    assertTrue(result.getStatusCode().is2xxSuccessful());
  }

  @Test
  void updateScheduleChange() {
    LessonDto inputValue = new LessonDto();
    ChangeDto expectedValue = new ChangeDto();
    Long id = 5L;
    when(service.update(id, inputValue)).thenReturn(expectedValue);
    ResponseEntity<ChangeDto> result = controller.updateScheduleChange(id, inputValue);
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
  void deleteScheduleChangeById_Exists() {
    Long id = 0L;
    when(service.deleteById(id)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteScheduleChange(id);
    assertTrue(result.getStatusCode().is2xxSuccessful());
    assertEquals("Deleting was successful", result.getBody());
  }

  @Test
  void deleteScheduleChangeById_DoesNotExist() {
    Long id = 0L;
    when(service.deleteById(id)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteScheduleChange(id);
    assertTrue(result.getStatusCode().is4xxClientError());
    assertEquals("Deleting wasn't successful", result.getBody());
  }
}
