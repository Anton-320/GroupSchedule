package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DateLessonListDto {
  private LocalDate date;
  private List<LessonListByGroupDto> lessons;

  @Data
  @AllArgsConstructor
  public static class LessonListByGroupDto {
    private Integer group;
    private List<LessonDto> lessons;
  }
}
