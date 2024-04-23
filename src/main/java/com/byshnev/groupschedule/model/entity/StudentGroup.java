package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup {
  @Id
  private Integer groupNumber;
  private Integer studentsAmount;
  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	//link to lessons
  private List<Lesson> lessons = new ArrayList<>();

  public StudentGroup(Integer groupNum) {
    this.groupNumber = groupNum;
  }

  public StudentGroup(Integer groupNumber, Integer studentsAmount) {
    this.groupNumber = groupNumber;
    this.studentsAmount = studentsAmount;
  }
}
