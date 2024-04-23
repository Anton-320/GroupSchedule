package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class GroupLessonListDto {
  private Integer group;
  private List<LessonListByDateDto> lessons;

  @Getter
  @AllArgsConstructor
  public static class LessonListByDateDto {
    private List<LessonDto> lesson;
    private LocalDate date;
  }
}
