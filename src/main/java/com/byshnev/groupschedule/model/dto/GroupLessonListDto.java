package com.byshnev.groupschedule.model.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Dto.
 * */
@Data
@AllArgsConstructor
public class GroupLessonListDto {
  private Integer group;
  private List<LessonListByDateDto> lessons;

  /**
   * Dto.
   * */
  @Data
  @AllArgsConstructor
  public static class LessonListByDateDto {
    private List<LessonDto> lesson;
    private LocalDate date;
  }
}
