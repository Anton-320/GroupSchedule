package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.cache.TeacherCache;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.model.entity.Lesson;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.utility.TeacherUtility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherService {
	private TeacherRepository repository;
	private TeacherCache cache;

	@Transactional
	public List<TeacherDto> findAllTeachers() {
		return repository.findAll().stream()
				.map((TeacherUtility::convertToDto))
				.toList();
	}

	@Transactional
	public List<TeacherDto> findTeachersBySurname(String surname) {
		return repository.findBySurname(surname).stream()
				.map((TeacherUtility::convertToDto))
				.toList();
	}

	@Transactional
	public TeacherDto findTeacherByUrlId (String urlId) {
		Teacher tmp = cache.get(urlId).orElse(null);
		if (tmp != null)
			return TeacherUtility.convertToDto(tmp);
		tmp = repository.findByUrlId(urlId);
		if (tmp != null) {
			cache.put(urlId, tmp);
			return TeacherUtility.convertToDto(tmp);
		}
		else return null;
	}

	@Transactional
	public TeacherDto add(TeacherDto teacherDto) {
		if (checkIfTeacherExists(teacherDto))
			return null;
		Teacher teacher = TeacherUtility.createEntityObjWithoutLink(teacherDto);
		cache.put(teacher.getUrlId(), teacher);
		return TeacherUtility.convertToDto(repository.save(teacher));
	}

	@Transactional
	public TeacherDto update(String urlId, TeacherDto teacher) {
		Teacher tmp = repository.findByUrlId(urlId);
		if (tmp == null)
			return null;
		tmp.setUrlId(teacher.getUrlId());
		tmp.setName(teacher.getName());
		tmp.setSurname(teacher.getSurname());
		tmp.setPatronymic(teacher.getPatronymic());
		tmp.setDegree(teacher.getDegree());
		tmp.setEmail(teacher.getEmail());
		cache.put(tmp.getUrlId(), tmp);
		return TeacherUtility.convertToDto(repository.save(tmp));
	}

	@Transactional
	public boolean delete(String urlId) {
		if (!repository.existsByUrlId(urlId))
			return false;
		cache.remove(urlId);
		Teacher teacher = repository.findByUrlId(urlId);
		teacher.getLessons().forEach(lesson -> lesson.getTeachers().remove(teacher));
		repository.delete(teacher);
		return true;
	}

	private boolean checkIfTeacherExists(TeacherDto teacher) {
		return repository.existsByUrlId(teacher.getUrlId())
				|| repository.existsByNameAndSurnameAndPatronymic(
						teacher.getName(),
						teacher.getSurname(),
						teacher.getPatronymic());
	}
}
