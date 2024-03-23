package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.service.changes.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule/teachers")
@AllArgsConstructor
public class TeacherController {

	private TeacherService service;

	@GetMapping("/all")
	public List<TeacherDto> getAllTeachers() {
		return service.findAllTeachers();
	}

	@GetMapping("/surn")
	public List<TeacherDto> getTeachersBySurname(@RequestParam(name = "surn") String surname) {
		return service.findTeachersBySurname(surname);
	}

	@GetMapping("/{urlId}")
	public TeacherDto getTeacherByUrlId(@PathVariable String urlId) {
		return service.findTeacherByUrlId(urlId);
	}

	@PostMapping
	public ResponseEntity<TeacherDto> addTeacher(@RequestBody TeacherDto teacherDto) {
		return null;
	}

	@PutMapping("/{urlId}")
	public ResponseEntity<TeacherDto> updateTeacher(@PathVariable String urlId) {
		return null;
	}

	@DeleteMapping("/{urlId}")
	public ResponseEntity<String> deleteTeacher(@PathVariable String urlId) {
		return null;
	}

}
