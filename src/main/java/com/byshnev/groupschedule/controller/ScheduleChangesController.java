package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.service.changes.LessonService;
import jakarta.validation.constraints.Positive;
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
  public List<GroupLessonListDto> getAllScheduleChanges() {
    return service.getAll();
  }

  @GetMapping("/{groupNumber}")
  public GroupLessonListDto getAllGroupScheduleChanges(@Positive @PathVariable(name = "groupNumber") Integer groupNum) {
    return service.getByGroup(groupNum);
  }

  @GetMapping
  public ResponseEntity<LessonDto> getById(@RequestParam Long id) {
    LessonDto tmp = service.getById(id);
    if (tmp != null)
      return new ResponseEntity<>(tmp, HttpStatus.FOUND);
    else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/{groupNumber}/{date}")
  public List<LessonDto> getGroupScheduleChangesByDate(@PathVariable(name = "date") String date, @PathVariable(name = "groupNumber") Integer groupNum) {
    return service.getByGroupAndDate(groupNum, date);
  }

  @GetMapping("/by_teacher/{urlId}")
  public List<DateLessonListDto> getScheduleChangesByTeacher(@PathVariable String urlId) {
    return service.getByTeacher(urlId);
  }

  @PostMapping
  public ResponseEntity<LessonDto> addScheduleChange(@RequestParam(name = "groupNum") Integer groupNum, @RequestParam(name = "date") String date, @RequestBody LessonDto lesson) {
    return ResponseEntity.ok(service.add(lesson, date, groupNum));
  }

  @PutMapping("/{id}")
  public ResponseEntity<LessonDto> updateScheduleChange(@PathVariable Long id, @RequestBody LessonDto lesson) {
    LessonDto tmp = service.update(id, lesson);
    return ResponseEntity.ok(tmp);
  }

  @DeleteMapping("/{groupNumber}")
  public ResponseEntity<String> deleteScheduleChanges(@Positive @PathVariable Integer groupNumber) {
    if(service.deleteByGroup(groupNumber))
      return ResponseEntity.accepted().body(SUCCESSFUL_DELETING);
    else return ResponseEntity.badRequest().body(NOT_SUCCESSFUL_DELETING);
  }

  @DeleteMapping("/{groupNumber}/{date}")
  public ResponseEntity<String> deleteScheduleChanges(@PathVariable Integer groupNumber, @PathVariable String date) {
    if (service.deleteByGroupAndDate(groupNumber, date))
      return ResponseEntity.accepted().body(SUCCESSFUL_DELETING);
    else
      return ResponseEntity.badRequest().body(NOT_SUCCESSFUL_DELETING);
  }

  @DeleteMapping("/{groupNumber}/{date}/{startTime}")
  public ResponseEntity<String> deleteScheduleChange(@PathVariable Integer groupNumber, @PathVariable String date, @PathVariable String startTime) {
    if (service.deleteByGroupAndDateAndTime(date, startTime, groupNumber))
      return ResponseEntity.accepted().body(SUCCESSFUL_DELETING);
    else
      return ResponseEntity.badRequest().body(NOT_SUCCESSFUL_DELETING);
  }
}
