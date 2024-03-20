package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Date {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NaturalId
	private LocalDate dateValue;
	@ManyToMany(mappedBy = "dates", cascade = {CascadeType.MERGE, CascadeType.PERSIST})		//link to student groups
	private List<StudentGroup> groups;
	@OneToMany(mappedBy = "date", cascade = CascadeType.ALL)	//link to lessons
	private List<Lesson> lessons;
	@ManyToMany(mappedBy = "dates", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Teacher> teachers;


	public Date(LocalDate date) {
		this.dateValue = date;
	}
}
