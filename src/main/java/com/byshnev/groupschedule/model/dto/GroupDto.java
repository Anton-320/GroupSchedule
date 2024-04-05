package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupDto {
	private Integer groupNum;
	private Integer studentsAmount;
}
