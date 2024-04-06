package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.service.changes.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {
	private GroupService service;

	@GetMapping("/all")
	public List<GroupDto> getAllGroups() {
		return service.getAllGroups();
	}

	@GetMapping("/{groupNum}")
	public ResponseEntity<GroupDto> getById(@PathVariable Integer groupNum) {
		GroupDto tmp = service.getGroupByNum(groupNum);
		if (tmp != null)
			return new ResponseEntity<>(tmp, HttpStatus.FOUND);
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping()
	public ResponseEntity<GroupDto> addGroup(@RequestBody GroupDto group) {
		GroupDto tmp = service.add(group);
		if (tmp == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else return new ResponseEntity<>(tmp, HttpStatus.CREATED);
	}

	@PutMapping("/{groupNum}")
	public ResponseEntity<GroupDto> updateGroup(@PathVariable Integer groupNum, @RequestBody GroupDto group) {
		GroupDto tmp = service.update(groupNum, group);
		if (tmp == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else return new ResponseEntity<>(tmp, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{groupNum}")
	public ResponseEntity<String> deleteGroup(@PathVariable Integer groupNum) {
		if (service.delete(groupNum))
			return new ResponseEntity<>("Deleting was successful", HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>("Deleting wasn't successful", HttpStatus.NOT_FOUND);
	}
}
