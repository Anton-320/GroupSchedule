package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.components.cache.AuditoriumCache;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * asjkdb.
 * */
@Service
@AllArgsConstructor
public class AuditoriumService {
  private AuditoriumRepository repository;
  private AuditoriumCache cache;

  /**
   * asjkdb.
   * */
  @Transactional
  public List<String> getAll() {
    return repository.findAll().stream()
        .map(Auditorium::getName)
        .collect(Collectors.toList());
  }

  /**
   * asjkdb.
   * */
  public String getById(Long id) {
    String tmpDto = cache.get(id).orElse(null);
    Auditorium tmp;
    if (tmpDto == null) {
      tmp = repository.findById(id).orElse(null);
      if (tmp != null) {
        cache.put(id, tmpDto);
        return tmp.getName();
      }
      return null;
    }
    return tmpDto;
  }

  /**
   * asjkdb.
   * */
  @Transactional
  public String create(String auditorium) {
    if (!repository.existsByName(auditorium)) {
      Auditorium tmp = repository.save(new Auditorium(auditorium));
      cache.put(tmp.getId(), auditorium);
      return tmp.getName();
    } else {
      return null;
    }
  }

  /**
   * asjkdb.
   * */
  @Transactional
  public String update(Long id, String auditorium) {
    Auditorium tmp = repository.findById(id).orElse(null);
    if (tmp != null) {
      tmp.setName(auditorium);
      cache.put(tmp.getId(), tmp.getName());
      return repository.save(tmp).getName();
    } else {
      return null;
    }
  }

  /**
   * asjkdb.
   * */
  @Transactional
  public boolean delete(Long id) {
    if (!repository.existsById(id)) {
      return false;
    }
    Auditorium tmp = repository.findById(id).orElse(null);
    if (tmp == null) {
      return false;
    }
    tmp.getLessons().forEach(lesson -> lesson.getAuditoriums().remove(tmp));
    repository.delete(tmp);
    cache.remove(id);
    return true;
  }
}
