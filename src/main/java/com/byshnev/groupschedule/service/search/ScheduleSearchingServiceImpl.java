package com.byshnev.groupschedule.service.search;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import com.byshnev.groupschedule.service.api.BsuirApiService;
import com.byshnev.groupschedule.service.utility.LessonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ScheduleSearchingServiceImpl implements ScheduleSearchingService {
	private LessonRepository lessonRepository;
	private GroupRepository groupRepository;

	private BsuirApiService bsuirApiService;

	public List<LessonDto> getSchedule(Integer groupNum, String dateInStr) throws JsonProcessingException {
		LocalDate date = LocalDate.parse(dateInStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		List<LessonDto> schedule = bsuirApiService.getScheduleFromBsuirApi(groupNum, date);
		List<Lesson> tmp = lessonRepository.findLessonsByGroupAndDate(groupRepository.findByGroupNum(groupNum), date);
		schedule = schedule.stream()
				.filter(lessonDto -> tmp.stream().noneMatch(lesson -> {

					return LocalTime
							.parse(lessonDto.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))
							.equals(lesson.getStartTime());

				}
				))
				.collect(Collectors.toList());
		if (tmp.size() > 0) {
			schedule.addAll(LessonUtility.convertToLessonDtoList(tmp));
		}
		return schedule;
	}
}