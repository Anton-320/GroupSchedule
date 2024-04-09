package com.byshnev.groupschedule.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LessonDto {
	private String name;
	private String subjectFullName;
	@NotNull
	private String startTime;
	private String endTime;
	private String note;
	private String lessonTypeAbbr;
	private List<String> auditoriums;
	private int subgroupNum;
	private List<TeacherDto> teachers;
}