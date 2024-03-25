package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.service.changes.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class GroupController {
	private GroupService service;

	@GetMapping("/all")
	public List<Integer> getAllGroups() {
		return service.getAllGroups();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Integer> getGroupById(@PathVariable Integer id) {
		Integer tmp = service.getById(id);
		if (tmp != null)
			return new ResponseEntity<>(tmp, HttpStatus.FOUND);
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping()
	public ResponseEntity<Integer> addGroup(@RequestBody Integer groupNum) {
		Integer tmp = service.add(groupNum);
		if (tmp == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<>(tmp, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Integer> updateGroup(@PathVariable Integer id, @RequestBody Integer groupNum) {
		Integer tmp = service.update(id, groupNum);
		if (tmp == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteGroup(@PathVariable Integer id) {
		if (service.delete(id))
			return new ResponseEntity<>("Deleting was successful", HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>("The resource is not found", HttpStatus.NOT_FOUND);
	}
}
