package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.service.changes.GroupService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {
  private GroupService service;

  @GetMapping("/all")
  public List<GroupDto> getAllGroups() {
    return service.getAllGroups();
  }

  @GetMapping("/{groupNumber}")
  public ResponseEntity<GroupDto> getById(@Min(100000) @Max(999999) @PathVariable Integer groupNumber) {
    GroupDto tmp = service.getGroupByNum(groupNumber);
    if (tmp != null) {
      return new ResponseEntity<>(tmp, HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping()
  public ResponseEntity<GroupDto> addGroup(@Min(100000) @Max(999999) @RequestBody GroupDto group) {
    GroupDto tmp = service.add(group);
    if (tmp == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(tmp, HttpStatus.CREATED);
    }
  }

  @PutMapping("/{groupNumber}")
  public ResponseEntity<GroupDto> updateGroup(@Min(100000) @Max(999999) @PathVariable Integer groupNumber,
                                              @RequestBody GroupDto group) {
    GroupDto tmp = service.update(groupNumber, group);
    if (tmp == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(tmp, HttpStatus.ACCEPTED);
    }
  }

  @DeleteMapping("/{groupNumber}")
  public ResponseEntity<String> deleteGroup(@Min(100000) @Max(999999) @PathVariable Integer groupNumber) {
    if (service.delete(groupNumber)) {
      return new ResponseEntity<>("Deleting was successful", HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>("Deleting wasn't successful", HttpStatus.NOT_FOUND);
    }
  }
}
