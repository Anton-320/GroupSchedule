package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LessonExtendedDto {
	private Integer groupNumber;
	private LocalDate date;
	private LessonDto lessonDtoList;
}
