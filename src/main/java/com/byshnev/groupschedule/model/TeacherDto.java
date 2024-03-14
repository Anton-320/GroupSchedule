package com.byshnev.groupschedule.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherDto {
	private String name;
	private String surname;
	private String patronymic;
	private String degree;
	private String email;
}