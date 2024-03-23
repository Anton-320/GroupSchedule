package com.byshnev.groupschedule.service.search;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ScheduleSearchingService {
	List<LessonDto> getSchedule(Integer groupNumber, String date) throws JsonProcessingException;
}