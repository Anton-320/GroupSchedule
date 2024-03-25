package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
	private TeacherRepository repository;

	public List<TeacherDto> findAllTeachers() {
		return repository.findAll().stream()
				.map((TeacherUtility::ConvertToDto))
				.collect(Collectors.toList());
	}

	public List<TeacherDto> findTeachersBySurname(String surname) {
		return repository.findBySurname(surname).stream()
				.map((TeacherUtility::ConvertToDto))
				.collect(Collectors.toList());
	}

	public TeacherDto findTeacherByUrlId (String urlId) {
		return TeacherUtility.ConvertToDto(repository.findByUrlId(urlId));
	}

	public TeacherDto add(TeacherDto teacherDto) {
		if (checkIfTeacherExists(teacherDto))
			return null;
		Teacher teacher = TeacherUtility.createEntityObjWithoutLink(teacherDto);
		return TeacherUtility.ConvertToDto(repository.save(teacher));
	}

	public TeacherDto update(String urlId, TeacherDto teacher) {
		Teacher tmp = repository.findByUrlId(urlId);
		tmp.setUrlId(teacher.getUrlId());
		tmp.setName(teacher.getName());
		tmp.setSurname(teacher.getSurname());
		tmp.setPatronymic(teacher.getPatronymic());
		tmp.setDegree(teacher.getDegree());
		tmp.setEmail(teacher.getEmail());
		return TeacherUtility.ConvertToDto(repository.save(tmp));
	}

	public boolean delete(String urlId) {
		return repository.deleteByUrlId(urlId);
	}

	private boolean checkIfTeacherExists(TeacherDto teacher) {
		return repository.existsByUrlId(teacher.getUrlId())
				|| repository.existsByNameAndSurnameAndPatronymic(
						teacher.getName(),
						teacher.getSurname(),
						teacher.getPatronymic());
	}
}
