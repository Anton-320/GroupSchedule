package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.service.changes.AuditoriumService;
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
public class AuditoriumControllerTest {
  @InjectMocks
  private AuditoriumController controller;

  @Mock
  private AuditoriumService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllAuditoriums() {
    List<String> expectedValue = new ArrayList<>();
    expectedValue.add("aud1");
    expectedValue.add("aud2");
    when(service.getAll()).thenReturn(expectedValue);
    List<String> result = controller.getAllAuditoriums();
    assertEquals(expectedValue, result);
  }

  @Test
  void getById_Exists() {
    Long id = 5L;
    String expectedValue = "514-5 к.";
    when(service.getById(id)).thenReturn(expectedValue);
    ResponseEntity<String> result = controller.getById(id);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void getById_DoesNotExist() {
    Long id = 5L;
    when(service.getById(id)).thenReturn(null);
    ResponseEntity<String> result = controller.getById(id);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void addAuditorium_DoesNotExist() {
    String expectedValue = "514-5 к.";
    when(service.create(expectedValue)).thenReturn(expectedValue);
    ResponseEntity<String> result = controller.addAuditorium(expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
  }

  @Test
  void addAuditorium_Exists() {
    String expectedValue = "514-5 к.";
    when(service.create(expectedValue)).thenReturn(null);
    ResponseEntity<String> result = controller.addAuditorium(expectedValue);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
  }

  @Test
  void updateAuditorium_Exists() {
    Long id = 5L;
    String expectedValue = "514-5 к.";
    when(service.update(id, expectedValue)).thenReturn(expectedValue);
    ResponseEntity<String> result = controller.updateAuditorium(id, expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.ACCEPTED.value(), result.getStatusCode().value());
  }

  @Test
  void updateAuditorium_DoesNotExist() {
    Long id = 5L;
    String expectedValue = "514-5 к.";
    when(service.update(id, expectedValue)).thenReturn(null);
    ResponseEntity<String> result = controller.updateAuditorium(id, expectedValue);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void deleteAuditorium_Exists() {
    Long id = 5L;
    when(service.delete(id)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteAuditorium(id);
    assertEquals("Deleting was successful", result.getBody());
    assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatusCode().value());
  }

  @Test
  void deleteAuditorium_DoesNotExist() {
    Long id = 5L;
    when(service.delete(id)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteAuditorium(id);
    assertEquals("Deleting wasn't successful", result.getBody());
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }
}
