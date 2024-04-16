package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * GroupDto.
 * */
@Data
@AllArgsConstructor
public class GroupDto {
  private Integer groupNumber;
  private Integer studentsAmount;
}
