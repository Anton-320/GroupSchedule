package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.service.changes.AuditoriumService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditoriums")
@AllArgsConstructor
public class AuditoriumController {
	private AuditoriumService service;

	@GetMapping("/all")
	public List<String> getAllAuditoriums() {
		return service.getAll();
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@PostMapping()
	public ResponseEntity<String> addAuditorium(@RequestBody String auditorium) {
		String tmp = service.create(auditorium);
		if (tmp == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateAuditorium(@PathVariable Long id, @RequestBody String auditorium) {
		String tmp = service.update(id, auditorium);
		if (tmp == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAuditorium(@PathVariable Long id) {
		if (service.delete(id))
			return new ResponseEntity<>("Deleting was successful", HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>("Deleting wasn't successful", HttpStatus.NOT_FOUND);
	}
}
