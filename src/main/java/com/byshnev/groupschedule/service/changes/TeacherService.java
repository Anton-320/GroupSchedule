package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
	private TeacherRepository repository;

	//get all teachers, that have changes
	public List<TeacherDto> findAllTeachers() {
		return repository.findAll().stream()
				.map(TeacherUtility::ConvertToDto)
				.collect(Collectors.toList());
	}

	public List<TeacherDto> findTeachersBySurname(String surname) {
		return repository.findBySurname(surname).stream()
				.map(TeacherUtility::ConvertToDto)
				.collect(Collectors.toList());
	}

	public TeacherDto findTeacherByUrlId (String urlId) {
		return TeacherUtility.ConvertToDto(repository.findByUrlId(urlId));
	}
}
