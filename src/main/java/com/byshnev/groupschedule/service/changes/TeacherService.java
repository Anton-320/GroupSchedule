package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.components.cache.TeacherCache;
import com.byshnev.groupschedule.model.dto.TeacherDto;
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

	public TeacherDto findTeacherByUrlId (String urlId) {
		TeacherDto tmpDto = cache.get(urlId).orElse(null);
		Teacher tmp;
		if (tmpDto != null)
			return tmpDto;
		tmp = repository.findByUrlId(urlId);
		if (tmp != null) {
			tmpDto = TeacherUtility.convertToDto(tmp);
			cache.put(urlId, tmpDto);
			return tmpDto;
		}
		else return null;
	}

	@Transactional
	public TeacherDto add(TeacherDto teacherDto) {
		if (checkIfTeacherExists(teacherDto))
			return null;
		Teacher teacher = TeacherUtility.createEntityObjWithoutLink(teacherDto);
		cache.put(teacherDto.getUrlId(), teacherDto);
		return TeacherUtility.convertToDto(repository.save(teacher));
	}

	@Transactional
	public TeacherDto update(String urlId, TeacherDto dto) {
		Teacher teacher = repository.findByUrlId(urlId);
		if (teacher == null)
			return null;
		teacher.setUrlId(dto.getUrlId());
		teacher.setName(dto.getName());
		teacher.setSurname(dto.getSurname());
		teacher.setPatronymic(dto.getPatronymic());
		teacher.setDegree(dto.getDegree());
		teacher.setEmail(dto.getEmail());
		cache.put(teacher.getUrlId(), dto);
		return TeacherUtility.convertToDto(teacher);
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
