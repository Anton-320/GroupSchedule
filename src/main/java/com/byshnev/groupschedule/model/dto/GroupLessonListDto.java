package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class GroupLessonListDto {
  private Integer group;
  private List<LessonListByDateDto> lessons;

  @Data
  @AllArgsConstructor
  public static class LessonListByDateDto {
    private List<LessonDto> lesson;
    private LocalDate date;
  }
}
