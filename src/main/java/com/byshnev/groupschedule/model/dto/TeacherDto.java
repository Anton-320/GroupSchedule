package com.byshnev.groupschedule.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeacherDto {
  @NotNull
  private String urlId;
  private String name;
  private String surname;
  private String patronymic;
  private String degree;
  private String email;
}
