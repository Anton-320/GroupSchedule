package com.byshnev.groupschedule.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * орпмора.
 * */
@Entity
@Data
@NoArgsConstructor
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
