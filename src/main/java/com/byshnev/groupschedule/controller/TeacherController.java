package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.service.changes.TeacherService;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/schedule/teachers")
@AllArgsConstructor
public class TeacherController {

  private TeacherService service;

  @GetMapping("/all")
  public List<TeacherDto> getAllTeachers() {
    return service.getAll();
  }

  @GetMapping("/{urlId}")
  public ResponseEntity<TeacherDto> getTeacherByUrlId(@NotEmpty @PathVariable String urlId) {
    TeacherDto tmp = service.getByUrlId(urlId);
    if (tmp != null) {
      return new ResponseEntity<>(tmp, HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<TeacherDto> addTeacher(@NotEmpty @RequestBody TeacherDto teacher) {
    TeacherDto tmp = service.add(teacher);
    if (tmp != null) {
      return new ResponseEntity<>(tmp, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{urlId}")
  public ResponseEntity<TeacherDto> updateTeacher(@NotEmpty @PathVariable String urlId, @RequestBody TeacherDto teacher) {
    TeacherDto tmp = service.update(urlId, teacher);
    if (tmp != null) {
      return new ResponseEntity<>(tmp, HttpStatus.ACCEPTED);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{urlId}")
  public ResponseEntity<String> deleteTeacher(@NotEmpty @PathVariable String urlId) {
    if (service.delete(urlId)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
