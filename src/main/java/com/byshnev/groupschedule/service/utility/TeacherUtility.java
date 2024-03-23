package com.byshnev.groupschedule.service.utility;

import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.model.entity.compositekey.FullName;
import com.byshnev.groupschedule.model.dto.TeacherDto;

public class TeacherUtility {
	public static TeacherDto ConvertToDto(Teacher teacher) {
		return new TeacherDto(teacher.getName(),
							  teacher.getSurname(),
							  teacher.getPatronymic(),
							  teacher.getDegree(),
							  teacher.getEmail());
	}

	public static Teacher createTeacherEntityWithoutLink(TeacherDto teacherDto) {
		return new Teacher(
				teacherDto.getName(),
				teacherDto.getSurname(),
				teacherDto.getPatronymic(),
				teacherDto.getDegree(),
				teacherDto.getEmail());
	}
}
