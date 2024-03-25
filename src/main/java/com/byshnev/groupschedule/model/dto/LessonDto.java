package com.byshnev.groupschedule.model.dto;

import com.byshnev.groupschedule.model.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class LessonDto {
	private String name;
	private String subjectFullName;
	private String startTime;
	private String endTime;
	private String note;
	private String lessonTypeAbbr;
	private List<String> auditoriums;
	private int subgroupNum;
	private List<TeacherDto> teachers;
}