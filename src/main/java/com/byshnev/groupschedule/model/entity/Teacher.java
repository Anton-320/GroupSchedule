package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers", uniqueConstraints = @UniqueConstraint(
		name = "fullname",
		columnNames = {"name", "surname", "patronymic"}))
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
	@Id
	private String urlId;
	private String name;
	private String surname;
	private String patronymic;
	private String degree;
	private String email;
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@JoinTable(name = "lessons_teachers",
			joinColumns = {@JoinColumn(name = "teacher_id")},
			inverseJoinColumns = {@JoinColumn(name = "lesson_id")}
	)
	private List<Lesson> lessons = new ArrayList<>();

	public Teacher(String urlId, String name, String surname, String patronymic, String degree, String email) {
		this.urlId = urlId;
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.degree = degree;
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Teacher teacher = (Teacher) o;
		return name.equals(teacher.name) && surname.equals(teacher.surname) && patronymic.equals(teacher.patronymic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname, patronymic);
	}
}
