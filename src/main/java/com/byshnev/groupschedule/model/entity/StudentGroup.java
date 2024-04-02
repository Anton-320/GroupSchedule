package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup {
	@Id
	private Integer groupNum;
	private Integer studentsAmount;
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	//link to lessons
	private List<Lesson> lessons;

	public StudentGroup(Integer groupNum) {
		this.groupNum = groupNum;
	}

	public StudentGroup(Integer groupNum, Integer studentsAmount) {
		this.groupNum = groupNum;
		this.studentsAmount = studentsAmount;
	}
}
