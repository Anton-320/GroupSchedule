package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.ChangeDto;
import com.byshnev.groupschedule.model.dto.DateChangeListDto;
import com.byshnev.groupschedule.model.dto.GroupChangeListDto;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.service.changes.LessonService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/changes")
@AllArgsConstructor
public class ScheduleChangesController {
  private LessonService service;
  private static final String SUCCESSFUL_DELETING = "Deleting was successful";
  private static final String NOT_SUCCESSFUL_DELETING = "Deleting wasn't successful";

  @GetMapping("/all")
  public List<GroupChangeListDto> getAllScheduleChanges() {
    return service.getAll();
  }

  @GetMapping("/{groupNumber}")
  public GroupChangeListDto getAllGroupScheduleChanges(@Min(100000) @Max(999999) @PathVariable(name = "groupNumber") Integer groupNum) {
    return service.getByGroup(groupNum);
  }

  @GetMapping
  public ResponseEntity<ChangeDto> getById(@RequestParam Long id) {
    ChangeDto tmp = service.getById(id);
    if (tmp != null) {
      return new ResponseEntity<>(tmp, HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{groupNumber}/{date}")
  public List<ChangeDto> getGroupScheduleChangesByDate(
      @PathVariable(name = "date") String date, @Min(100000) @Max(999999) @PathVariable(name = "groupNumber") Integer groupNum) {
    return service.getByGroupAndDate(groupNum, date);
  }

  @GetMapping("/by_teacher/{urlId}")
  public List<DateChangeListDto> getScheduleChangesByTeacher(@PathVariable String urlId) {
    return service.getByTeacher(urlId);
  }

  @PostMapping
  public ResponseEntity<ChangeDto> addScheduleChange(
      @Min(100000) @Max(999999) @RequestParam(name = "groupNum") Integer groupNum,
      @RequestParam(name = "date") String date, @NotNull @RequestBody LessonDto lesson) {
    return ResponseEntity.ok(service.add(lesson, date, groupNum));
  }

  @PostMapping("/batch")
  public ResponseEntity<List<ChangeDto>> addScheduleChanges(
      @Min(100000) @Max(999999) @RequestParam(name = "groupNum") Integer groupNum,
      @RequestParam(name = "date") String date, @NotNull @RequestBody List<LessonDto> lessons) {
    return ResponseEntity.ok(service.addBatch(groupNum, date, lessons));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ChangeDto> updateScheduleChange(@PathVariable Long id, @RequestBody LessonDto lesson) {
    ChangeDto tmp = service.update(id, lesson);
    return ResponseEntity.ok(tmp);
  }

  @DeleteMapping("/by_group")
  public ResponseEntity<String> deleteScheduleChanges(@Min(100000) @Max(999999) @RequestParam(name = "groupNum") Integer groupNumber) {
    if (service.deleteByGroup(groupNumber)) {
      return ResponseEntity.accepted().body(SUCCESSFUL_DELETING);
    } else {
      return ResponseEntity.badRequest().body(NOT_SUCCESSFUL_DELETING);
    }
  }

  @DeleteMapping("/by_group_and_date")
  public ResponseEntity<String> deleteScheduleChanges(@Min(100000) @Max(999999) @RequestParam(name = "groupNum") Integer groupNumber, @RequestParam String date) {
    if (service.deleteByGroupAndDate(groupNumber, date)) {
      return ResponseEntity.accepted().body(SUCCESSFUL_DELETING);
    } else {
      return ResponseEntity.badRequest().body(NOT_SUCCESSFUL_DELETING);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteScheduleChange(@PathVariable Long id) {
    if (service.deleteById(id)) {
      return ResponseEntity.accepted().body(SUCCESSFUL_DELETING);
    } else {
      return ResponseEntity.badRequest().body(NOT_SUCCESSFUL_DELETING);
    }
  }

}
