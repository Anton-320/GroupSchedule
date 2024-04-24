package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DateLessonListDto {
  private LocalDate date;
  private List<LessonListByGroupDto> lessons;

  @Getter
  @AllArgsConstructor
  public static class LessonListByGroupDto {
    private Integer group;
    private List<LessonDto> lessons;
  }
}
