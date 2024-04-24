package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.service.changes.GroupService;
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
public class GroupControllerTest {
  @InjectMocks
  private GroupController controller;

  @Mock
  private GroupService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllGroups() {
    List<GroupDto> expectedValue = new ArrayList<>();
    expectedValue.add(new GroupDto(250501, 21));
    when(service.getAllGroups()).thenReturn(expectedValue);
    List<GroupDto> result = controller.getAllGroups();
    assertEquals(expectedValue, result);
  }

  @Test
  void getById_Exists() {
    GroupDto expectedValue = new GroupDto(250501, 21);
    when(service.getGroupByNumber(expectedValue.getGroupNumber())).thenReturn(expectedValue);
    ResponseEntity<GroupDto> result = controller.getById(expectedValue.getGroupNumber());
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void getById_DoesNotExist() {
    Integer groupNumber = 250501;
    when(service.getGroupByNumber(groupNumber)).thenReturn(null);
    ResponseEntity<GroupDto> result = controller.getById(groupNumber);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void addGroup_DoesNotExist() {
    GroupDto expectedValue = new GroupDto(250501, 24);
    when(service.add(expectedValue)).thenReturn(expectedValue);
    ResponseEntity<GroupDto> result = controller.addGroup(expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
  }

  @Test
  void addGroup_AlreadyExists() {
    GroupDto expectedValue = new GroupDto(250501, 24);
    when(service.add(expectedValue)).thenReturn(null);
    ResponseEntity<GroupDto> result = controller.addGroup(expectedValue);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
  }

  @Test
  void updateGroup_Exists() {
    GroupDto expectedValue = new GroupDto(250501, 24);
    when(service.update(expectedValue.getGroupNumber(), expectedValue)).thenReturn(expectedValue);
    ResponseEntity<GroupDto> result = controller.updateGroup(expectedValue.getGroupNumber(), expectedValue);
    assertEquals(expectedValue, result.getBody());
    assertEquals(HttpStatus.ACCEPTED.value(), result.getStatusCode().value());
  }

  @Test
  void updateGroup_DoesNotExist() {
    GroupDto expectedValue = new GroupDto(250501, 24);
    when(service.update(expectedValue.getGroupNumber(), expectedValue)).thenReturn(null);
    ResponseEntity<GroupDto> result = controller.updateGroup(expectedValue.getGroupNumber(), expectedValue);
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }

  @Test
  void deleteGroup_Exists() {
    Integer groupNumber = 250501;
    when(service.delete(groupNumber)).thenReturn(true);
    ResponseEntity<String> result = controller.deleteGroup(groupNumber);
    assertEquals("Deleting was successful", result.getBody());
    assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatusCode().value());
  }

  @Test
  void deleteGroup_DoesNotExist() {
    Integer groupNumber = 250501;
    when(service.delete(groupNumber)).thenReturn(false);
    ResponseEntity<String> result = controller.deleteGroup(groupNumber);
    assertEquals("Deleting wasn't successful", result.getBody());
    assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode().value());
  }
}
