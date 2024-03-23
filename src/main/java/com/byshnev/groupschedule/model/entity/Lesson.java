package com.byshnev.groupschedule.model.entity;


import com.byshnev.groupschedule.model.dto.LessonDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table (name = "lessons")
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "group_id")
	private StudentGroup group;
	private LocalDate date;
	private String name;
	private String subjectFullName;
	private LocalTime startTime;
	private LocalTime endTime;
	private String note;
	private String lessonTypeAbbr;
	@ManyToMany(mappedBy = "lessons", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<Auditorium> auditoriums = new ArrayList<>();
	private int subgroupNum;
	@ManyToMany(mappedBy = "lessons", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<Teacher> teachers = new ArrayList<>();

//	public Lesson(LessonDto lessonDto) {
//		this.name = lessonDto.getName();
//		this.subjectFullName = lessonDto.getSubjectFullName();
//		this.startTime = LocalTime.parse(
//				lessonDto.getStartTime(),
//				DateTimeFormatter.ofPattern("HH:mm"));
//		this.endTime = LocalTime.parse(
//				lessonDto.getEndTime(),
//				DateTimeFormatter.ofPattern("HH:mm"));
//		this.note = lessonDto.getNote();
//		this.lessonTypeAbbr = lessonDto.getLessonTypeAbbr();
//		this.subgroupNum = lessonDto.getSubgroupNum();
//		this.teachers = lessonDto.getTeachers().stream()
//				.map(Teacher::new)
//				.collect(Collectors.toList());
//		this.auditoriums = lessonDto.getAuditoriums().stream()
//				.map(Auditorium::new).collect(Collectors.toList());
//	}

	public Lesson(StudentGroup group, String name, String subjectFullName, LocalDate date, LocalTime startTime, LocalTime endTime, String note, String lessonTypeAbbr, List<Auditorium> auditoriums, int subgroupNum, List<Teacher> teachers) {
		this.group = group;
		this.name = name;
		this.subjectFullName = subjectFullName;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.note = note;
		this.lessonTypeAbbr = lessonTypeAbbr;
		this.auditoriums = auditoriums;
		this.subgroupNum = subgroupNum;
		this.teachers = teachers;
	}


}

