package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.DateLessonListDto;
import com.byshnev.groupschedule.model.dto.GroupLessonListDto;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.service.changes.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/changes")
@AllArgsConstructor
public class ScheduleChangesController {
	private LessonService service;

	@GetMapping("/all")
	public List<GroupLessonListDto> getAllScheduleChanges() {
		return null;
	}

	@GetMapping("/by_date/{date}")
	public DateLessonListDto getAllScheduleChangesByDate(@PathVariable String date) {
		return service.getByDate(date);
	}

	@GetMapping("/{groupNumber}")
	public GroupLessonListDto getAllGroupScheduleChanges(@PathVariable(name = "groupNumber") Integer groupNum) {
		return service.getByGroup(groupNum);
	}

	@GetMapping("/{groupNum}/{date}")
	public List<LessonDto> getGroupScheduleChangesByDate(@PathVariable(name = "date") String date, @PathVariable(name = "groupNum") Integer groupNum) {
		return service.getByGroupAndDate(groupNum, date);
	}

	@GetMapping("/by_teacher")
	public List<DateLessonListDto> getScheduleChangesByTeacher(@RequestParam String name, @RequestParam(name = "surn") String surname, @RequestParam(name = "patr") String patronymic) {
		return service.getByTeacher(name, surname, patronymic);
	}

	@PostMapping
	public ResponseEntity<LessonDto> addScheduleChange(@RequestParam(name = "grNum") Integer groupNum, @RequestParam String date, @RequestBody LessonDto lesson) {
		return ResponseEntity.ok(service.add(lesson, date, groupNum));
	}

	@PutMapping
	public ResponseEntity<LessonDto> updateScheduleChange(@RequestParam(name = "grNum") Integer groupNum, @RequestParam String date, @RequestBody LessonDto lesson) {
		return ResponseEntity.ok(service.update(date, lesson.getStartTime(), lesson, groupNum));
	}

	@DeleteMapping("/{groupNum}/{date}")
	public ResponseEntity<String> deleteScheduleChange(@PathVariable Integer groupNum, @PathVariable String date) {
		if (service.deleteByDate(groupNum, date))
			return ResponseEntity.ok("Deleting was successful");
		else
			return ResponseEntity.ok("Deleting wasn't successful");
	}

	@DeleteMapping("/{groupNum}/{date}/{startTime}")
	public ResponseEntity<String> deleteScheduleChange(@PathVariable Integer groupNum, @PathVariable String date, @PathVariable String startTime) {
		if (service.deleteByDateAndTime(date, startTime, groupNum))
			return ResponseEntity.ok("Deleting was successful");
		else
			return ResponseEntity.ok("Deleting wasn't successful");
	}
}
