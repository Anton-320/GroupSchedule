package com.byshnev.groupschedule.service.utility;


import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.model.entity.Teacher;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TeacherUtility {

	public static Teacher createEntityObjWithoutLink(TeacherDto teacherDto) {
		return new Teacher(teacherDto.getUrlId(),
						   teacherDto.getName(),
						   teacherDto.getSurname(),
						   teacherDto.getPatronymic(),
						   teacherDto.getDegree(),
						   teacherDto.getEmail()
		);
	}

	public static TeacherDto ConvertToDto(Teacher teacher) {
		return new TeacherDto(
				teacher.getUrlId(),
				teacher.getName(),
				teacher.getSurname(),
				teacher.getPatronymic(),
				teacher.getDegree(),
				teacher.getEmail());
	}


}
