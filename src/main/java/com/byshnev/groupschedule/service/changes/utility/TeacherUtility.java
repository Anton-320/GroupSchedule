package com.byshnev.groupschedule.service.changes.utility;

import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.model.entity.compositekey.FullName;
import com.byshnev.groupschedule.model.dto.TeacherDto;

public class TeacherUtility {
	public static TeacherDto ConvertToDto(Teacher teacher) {
		return new TeacherDto(teacher.getFullname().getName(),
							  teacher.getFullname().getSurname(),
							  teacher.getFullname().getPatronymic(),
							  teacher.getDegree(),
							  teacher.getEmail());
	}

	public static Teacher ConvertToTeacherEntity(TeacherDto teacherDto) {
		return new Teacher(
				new FullName(
						teacherDto.getName(),
						teacherDto.getSurname(),
						teacherDto.getPatronymic()),
				teacherDto.getDegree(),
				teacherDto.getEmail());
	}
}
