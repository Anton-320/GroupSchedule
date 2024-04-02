package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
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

	public LessonDto(String name, String subjectFullName, String startTime, String endTime, String note, String lessonTypeAbbr, List<String> auditoriums, int subgroupNum, List<TeacherDto> teachers) {
		this.name = name;
		this.subjectFullName = subjectFullName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.note = note;
		this.lessonTypeAbbr = lessonTypeAbbr;
		this.auditoriums = auditoriums;
		this.subgroupNum = subgroupNum;
		this.teachers = teachers;
	}
}