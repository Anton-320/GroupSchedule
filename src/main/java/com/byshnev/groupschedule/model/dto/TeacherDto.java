package com.byshnev.groupschedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeacherDto {
	private String urlId;
	private String name;
	private String surname;
	private String patronymic;
	private String degree;
	private String email;
}
