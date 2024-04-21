package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auditorium {
  @Id
  @GeneratedValue
  private Long id;
  @NaturalId
  private String name;
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "lessons_auditoriums",
      joinColumns = {@JoinColumn(name = "auditorium_id")},
      inverseJoinColumns = {@JoinColumn(name = "lesson_id")})
  List<Lesson> lessons = new ArrayList<>();

  public Auditorium(String dtoAuditorium) {
    this.name = dtoAuditorium;
  }
}
