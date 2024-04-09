package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.service.search.ScheduleSearchingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/schedule_gr")
@AllArgsConstructor
public class ScheduleSearchingController {

	private ScheduleSearchingService service;

	@GetMapping
	public List<LessonDto> getSchedule(@Positive @RequestParam Integer groupNumber, @RequestParam String date) {
		try {
			return service.getSchedule(groupNumber, date);
		} catch (JsonProcessingException e) {
			return Collections.emptyList();
		}
	}
}