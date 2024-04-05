package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.service.changes.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

	@GetMapping("/{urlId}")
	public TeacherDto getTeacherByUrlId(@PathVariable String urlId) {
		return service.findTeacherByUrlId(urlId);
	}

	@PostMapping
	public ResponseEntity<TeacherDto> addTeacher(@RequestBody TeacherDto teacher) {
		TeacherDto tmp = service.add(teacher);
		if (tmp != null)
			return new ResponseEntity<>(tmp, HttpStatus.CREATED);
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/{urlId}")
	public ResponseEntity<TeacherDto> updateTeacher(@PathVariable String urlId, @RequestBody TeacherDto teacher) {
		TeacherDto tmp = service.update(urlId, teacher);
		if (tmp != null)
			return new ResponseEntity<>(tmp, HttpStatus.ACCEPTED);
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{urlId}")
	public ResponseEntity<String> deleteTeacher(@PathVariable String urlId) {
		if (service.delete(urlId))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
