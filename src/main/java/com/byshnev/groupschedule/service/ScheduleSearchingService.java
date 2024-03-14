package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.model.LessonDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ScheduleSearchingService {
	List<LessonDto> getSchedule(Integer group, String date) throws JsonProcessingException;
}