package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class StudentGroup {
	@Id
	@GeneratedValue
	private Long id;
	@NaturalId
	private Integer groupNum;
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	//link to lessons
	private List<Lesson> lessons;

	public StudentGroup(Integer groupNum) {
		this.groupNum = groupNum;
	}
}
