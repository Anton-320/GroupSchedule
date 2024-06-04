package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class GroupChangeListDto {
  private Integer group;
  private List<ChangeListByDateDto> lessons;

  @Getter
  @AllArgsConstructor
  public static class ChangeListByDateDto {
    private List<ChangeDto> lesson;
    private LocalDate date;
  }
}
