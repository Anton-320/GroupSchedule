package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class StudentGroup {
	@Id
	private Long id;
	@NaturalId
	private Integer groupNum;
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)	//link to lessons
	private List<Lesson> lessons;
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "dates_groups",
			joinColumns = {@JoinColumn(name = "group_id")},
			inverseJoinColumns = {@JoinColumn(name = "date_id")})
	private List<Date> dates;

	public StudentGroup(Integer groupNum) {
		this.groupNum = groupNum;
	}
}
