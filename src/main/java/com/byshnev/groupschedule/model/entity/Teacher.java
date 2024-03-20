package com.byshnev.groupschedule.model.entity;

import com.byshnev.groupschedule.model.entity.compositekey.FullName;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Teacher {
	@EmbeddedId
	private FullName fullname;
	private String degree;
	private String email;
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinTable(name = "lessons_teachers",
			joinColumns = { @JoinColumn(name = "teacher_name"),
					@JoinColumn(name = "teacher_surname"),
					@JoinColumn(name = "teacher_patronymic")},
			inverseJoinColumns = {@JoinColumn(name = "lesson_id")}
	)
	private List<Lesson> lessons = new ArrayList<>();
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "dates_teachers",
			joinColumns = { @JoinColumn(name = "teacher_name"),
					@JoinColumn(name = "teacher_surname"),
					@JoinColumn(name = "teacher_patronymic")},
			inverseJoinColumns = @JoinColumn(name = "date_id"))
	private List<Date> dates;
	public Teacher(FullName fullName, String degree, String email) {
		this.fullname = fullName;
		this.degree = degree;
		this.email = email;
	}

	public Teacher(TeacherDto teacherDto) {
		this.fullname = new FullName(
				teacherDto.getName(),
				teacherDto.getSurname(),
				teacherDto.getPatronymic());
		this.degree = teacherDto.getDegree();
		this.email = teacherDto.getEmail();
	}
}
