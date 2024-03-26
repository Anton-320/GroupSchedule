package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.cache.AuditoriumCache;
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
	private AuditoriumCache cache;

	public List<String> getAll() {
		return repository.findAll().stream()
				.map(Auditorium::getAuditorium)
				.collect(Collectors.toList());
	}

	public String getById(Long id) {
		Auditorium tmp = cache.get(id).orElse(null);
		if (tmp == null) {
			tmp = repository.findById(id).orElse(null);
			if (tmp != null) {
				cache.put(id, tmp);
				return tmp.getAuditorium();
			}
			return null;
		}
		return tmp.getAuditorium();
	}

	public String create(String auditorium) {
		if (!repository.existsByAuditorium(auditorium)) {
			Auditorium tmp = repository.save(new Auditorium(auditorium));
			cache.put(tmp.getId(), tmp);
			return tmp.getAuditorium();
		}
		else return null;
	}

	public String update(Long id, String auditorium) {
		Auditorium tmp = repository.findById(id).orElse(null);
		if (tmp != null) {
			tmp.setAuditorium(auditorium);
			cache.put(tmp.getId(),tmp);
			return repository.save(tmp).getAuditorium();
		}
		else return null;
	}

	public boolean delete(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			cache.remove(id);
			return true;
		}
		else return false;
	}
}
