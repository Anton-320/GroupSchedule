package com.byshnev.groupschedule.service.search;

import com.byshnev.groupschedule.cache.ScheduleGettingCache;
import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.api.BsuirApiService;
import com.byshnev.groupschedule.service.utility.LessonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ScheduleSearchingService {
	private LessonRepository lessonRepository;
	private BsuirApiService bsuirApiService;
	private ScheduleGettingCache cache;

	public List<LessonDto> getSchedule(Integer groupNum, String dateInStr) throws JsonProcessingException {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		String key = groupNum.toString() + dateInStr;
		List<LessonDto> schedule = cache.get(key).orElse(null);
		if (schedule == null) {
			schedule = bsuirApiService.getScheduleFromBsuirApi(groupNum, date);
			if (cache.contains(key))
				cache.put(key, schedule);
		}
		List<Lesson> changes = lessonRepository.findLessonsByGroupAndDate(groupNum, date);
		if (schedule != null) {
			schedule = schedule.stream()
					.filter(lessonDto -> changes.stream().noneMatch(lesson -> {

						return LocalTime
								.parse(lessonDto.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))
								.equals(lesson.getStartTime());

					}
					))
					.collect(Collectors.toList());
		}
		else {
			schedule = new ArrayList<>();

		}
		if (changes.isEmpty()) {
			return schedule;
		}
		schedule.addAll(LessonUtility.convertToLessonDtoList(changes));
		return schedule;
	}
}