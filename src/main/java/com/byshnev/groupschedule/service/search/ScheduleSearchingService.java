package com.byshnev.groupschedule.service.search;

import com.byshnev.groupschedule.components.cache.ScheduleGettingCache;
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
import java.util.Comparator;
import java.util.List;


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
			cache.put(key, schedule);
		}
		List<Lesson> changes = lessonRepository.findLessonsByGroupAndDate(groupNum, date);
		if (schedule != null) {
			schedule = schedule.stream()
					.filter(lessonDto -> changes.stream().noneMatch(lesson -> LocalTime
							.parse(lessonDto.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))
							.equals(lesson.getStartTime())
					))
					.toList();
		}
		else {
			schedule = new ArrayList<>();
		}
		if (changes.isEmpty()) {
			return schedule;
		}

		schedule = new ArrayList<>(schedule);	//VERY IMPORTANT
		schedule.addAll(LessonUtility.convertToLessonDtoList(changes.stream()
						 .sorted(Comparator.comparing(Lesson::getStartTime)).toList()));
		return schedule;
	}
}