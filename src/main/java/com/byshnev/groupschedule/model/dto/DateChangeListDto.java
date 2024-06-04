package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DateChangeListDto {
  private LocalDate date;
  private List<ChangeListByGroupDto> lessons;

  @Getter
  @AllArgsConstructor
  public static class ChangeListByGroupDto {
    private Integer group;
    private List<ChangeDto> lessons;
  }
}
