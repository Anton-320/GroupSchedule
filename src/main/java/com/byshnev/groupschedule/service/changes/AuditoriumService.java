package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuditoriumService {
	private AuditoriumRepository repository;

	public List<String> getAll() {
		return repository.findAll().stream()
				.map(Auditorium::getAuditorium)
				.collect(Collectors.toList());
	}

	public String getById(Long id) {
		Auditorium tmp = repository.findById(id).orElse(null);
		if (tmp != null)
			return tmp.getAuditorium();
		else return null;
	}

	public String create(String auditorium) {
		if (!repository.existsByAuditorium(auditorium))
			return repository.save(new Auditorium(auditorium)).getAuditorium();
		else return null;
	}

	public String update(Long id, String auditorium) {
		Auditorium tmp = repository.findById(id).orElse(null);
		if (tmp != null) {
			tmp.setAuditorium(auditorium);
			return repository.save(tmp).getAuditorium();
		}
		else return null;
	}

	public boolean delete(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		else return false;
	}
}
