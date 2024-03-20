package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.LessonExtendedDto;
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

	@GetMapping("/group/date")
	public List<LessonDto> getGroupScheduleChangesByDate(@RequestParam(name = "date") String date, @RequestParam(name = "grNum") Integer groupNum) {
		return service.getByGroupAndDate(groupNum, date);
	}

	@GetMapping("/group")
	public List<LessonExtendedDto> getAllGroupScheduleChanges(@RequestParam(name = "grNum") Integer groupNum) {
		return service.getByGroup(groupNum);
	}

	@GetMapping("/date")
	public List<LessonExtendedDto> getScheduleChangesByTeacher(@RequestParam String name, @RequestParam(name = "surn") String surname, @RequestParam(name = "patr") String patronymic) {
		return service.getByTeacher(name, surname, patronymic);
	}

	@PostMapping
	public ResponseEntity<LessonExtendedDto> addScheduleChange(@RequestParam(name = "grNum") Integer groupNum, @RequestParam String date, @RequestBody LessonDto lesson) {
		return ResponseEntity.ok(service.add(lesson, date, groupNum));
	}

	@PutMapping
	public ResponseEntity<LessonExtendedDto> updateScheduleChange(@RequestParam(name = "grNum") Integer groupNum, @RequestParam String date, @RequestBody LessonDto lesson) {
		return ResponseEntity.ok(service.update(date, lesson.getStartTime(), lesson, groupNum));
	}

}
