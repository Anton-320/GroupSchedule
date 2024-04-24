package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.service.changes.TeacherService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {
  @InjectMocks
  private TeacherController controller;

  @Mock
  private TeacherService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllTeachers() {
    List<TeacherDto> expectedValues = new ArrayList<>();
    expectedValues.add(createTeacherDto());
    when(service.getAll()).thenReturn(expectedValues);
    List<TeacherDto> result = controller.getAllTeachers();
    assertEquals(expectedValues, result);
  }

  @Test
  void getTeacherByUrlId_Exists() {
    TeacherDto expectedValue = createTeacherDto();
    String urlId = expectedValue.getUrlId();
    when(service.getByUrlId(urlId)).thenReturn(expectedValue);
    ResponseEntity<TeacherDto> result = controller.getTeacherByUrlId(urlId);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void getTeacherByUrlId_DoesNotExist() {
    String urlId = "l-podenok";
    when(service.getByUrlId(urlId)).thenReturn(null);
    ResponseEntity<TeacherDto> result = controller.getTeacherByUrlId(urlId);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void addTeacher_DoesNotExist() {
    TeacherDto expectedValue = createTeacherDto();
    when(service.add(expectedValue)).thenReturn(expectedValue);
    ResponseEntity<TeacherDto> result = controller.addTeacher(expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
  }

  @Test
  void addTeacher_Exists() {
    TeacherDto expectedValue = createTeacherDto();
    when(service.add(expectedValue)).thenReturn(null);
    ResponseEntity<TeacherDto> result = controller.addTeacher(expectedValue);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
  }

  @Test
  void updateTeacher_Exists() {
    TeacherDto expectedValue = createTeacherDto();
    String urlId = expectedValue.getUrlId();
    when(service.update(urlId, expectedValue)).thenReturn(expectedValue);
    ResponseEntity<TeacherDto> result = controller.updateTeacher(urlId, expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.ACCEPTED.value(), result.getStatusCode().value());
  }

  @Test
  void updateTeacher_DoesNotExist() {
    TeacherDto expectedValue = createTeacherDto();
    String urlId = expectedValue.getUrlId();
    when(service.update(urlId, expectedValue)).thenReturn(null);
    ResponseEntity<TeacherDto> result = controller.updateTeacher(urlId, expectedValue);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void deleteTeacher_Exists() {
    String urlId = "l-podenok";
    when(service.delete(urlId)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteTeacher(urlId);
    assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatusCode().value());
  }

  @Test
  void deleteTeacher_DoesNotExist() {
    String urlId = "l-podenok";
    when(service.delete(urlId)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteTeacher(urlId);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  TeacherDto createTeacherDto() {
    return new TeacherDto(
        "nataly", "Наталья", "Смирнова",
        "Анатольевна", "", "zismirnova@bsuir.by");
  }
}
