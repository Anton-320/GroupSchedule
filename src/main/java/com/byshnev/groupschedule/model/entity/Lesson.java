package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
  @ManyToMany(mappedBy = "lessons", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Auditorium> auditoriums = new ArrayList<>();
  private Integer subgroupNum;
  @ManyToMany(mappedBy = "lessons", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Teacher> teachers = new ArrayList<>();

  /**
   * Comment.
   * */
  public Lesson(String name, String subjectFullName, LocalDate date,
                String note, String lessonTypeAbbr, int subgroupNum) {
    this.name = name;
    this.subjectFullName = subjectFullName;
    this.date = date;
    this.note = note;
    this.lessonTypeAbbr = lessonTypeAbbr;
    this.subgroupNum = subgroupNum;
  }
}

