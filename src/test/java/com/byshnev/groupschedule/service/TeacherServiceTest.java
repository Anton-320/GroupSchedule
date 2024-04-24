package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.TeacherCache;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.byshnev.groupschedule.model.entity.Teacher;
import com.byshnev.groupschedule.repository.TeacherRepository;
import com.byshnev.groupschedule.service.changes.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
  @InjectMocks
  private TeacherService service;

  @Mock
  private TeacherRepository repository;

  @Mock
  private TeacherCache cache;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll() {
    List<Teacher> foundList = List.of(
        new Teacher(
            "l-podenok",
            "Леонид",
            "Поденок",
            "Петрович",
            "",
            null),
        new Teacher(
            "nataly",
            "Наталья",
            "Смирнова",
            "Анатольевна",
            "",
            "zismirnova@bsuir.by"
        )
    );
    when(repository.findAll()).thenReturn(foundList);
    List<TeacherDto> finalResult = service.getAll();
    verify(repository, times(1)).findAll();
    assertEquals(finalResult.size(), foundList.size());
  }

  @Test
  void getByUrlId_ExistsInCache() {
    String urlId = "l-podenok";
    TeacherDto foundValue = new TeacherDto(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null);
    when(cache.get(urlId)).thenReturn(Optional.of(foundValue));
    TeacherDto finalResult = service.getByUrlId(urlId);
    verify(cache, times(1)).get(urlId);
    verify(repository, never()).findByUrlId(any());
    verify(cache, never()).put(any(), any());
    assertEquals(finalResult, foundValue);
  }

  @Test
  void getByUrlId_ExistsInRepository() {
    String urlId = "l-podenok";
    Teacher foundValue = new Teacher(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null,
        new ArrayList<>());
    when(cache.get(urlId)).thenReturn(Optional.empty());
    when(repository.findByUrlId(urlId)).thenReturn(foundValue);
    service.getByUrlId(urlId);
    verify(cache, times(1)).get(urlId);
    verify(repository, times(1)).findByUrlId(urlId);
    verify(cache, times(1)).put(eq(urlId), any());
  }

  @Test
  void getByUrlId_DoesNotExist() {
    String urlId = "l-podenok";
    when(cache.get(urlId)).thenReturn(Optional.empty());
    when(repository.findByUrlId(urlId)).thenReturn(null);
    TeacherDto finalResult = service.getByUrlId(urlId);
    verify(cache, times(1)).get(urlId);
    verify(repository, times(1)).findByUrlId(urlId);
    verify(cache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void add_DoesNotExist() {
    String urlId = "l-podenok";
    Teacher addedEntity = new Teacher(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null);
    TeacherDto addedDto = new TeacherDto(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null);
    when(repository.existsByUrlId(urlId)).thenReturn(false);
    when(repository.existsByNameAndSurnameAndPatronymic(
        addedEntity.getName(),
        addedEntity.getSurname(),
        addedEntity.getPatronymic())).thenReturn(false);
    when(repository.save(addedEntity)).thenReturn(addedEntity);
    service.add(addedDto);
    verify(repository, times(1)).save(any());
    verify(cache, times(1)).put(urlId, addedDto);
  }

  @Test
  void add_Exists() {
    String urlId = "l-podenok";
    TeacherDto addedDto = new TeacherDto(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null);
    when(repository.existsByUrlId(urlId)).thenReturn(true);
    TeacherDto finalResult = service.add(addedDto);
    verify(repository, never()).save(any());
    verify(cache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void update_Exists() {
    String urlId = "nataly";
    TeacherDto newValue = new TeacherDto(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null);
    Teacher oldValue = new Teacher(
        urlId,
        "Наталья",
        "Смирнова",
        "Анатольевна",
        "",
        "zismirnova@bsuir.by"
    );
    when(repository.findByUrlId(urlId)).thenReturn(oldValue);
    service.update(urlId, newValue);
    verify(repository, times(1)).findByUrlId(urlId);
    verify(cache, times(1)).put(newValue.getUrlId(), newValue);
  }

  @Test
  void update_DoesNotExists() {
    String urlId = "nataly";
    TeacherDto newValue = new TeacherDto(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null);
    when(repository.findByUrlId(urlId)).thenReturn(null);
    TeacherDto finalResult = service.update(urlId, newValue);
    verify(repository, times(1)).findByUrlId(urlId);
    verify(cache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void delete_Exists() {
    String urlId = "l-podenok";
    Teacher foundTeacher = new Teacher(
        "l-podenok",
        "Леонид",
        "Поденок",
        "Петрович",
        "",
        null,
        new ArrayList<>());
    when(repository.existsByUrlId(urlId)).thenReturn(true);
    when(repository.findByUrlId(urlId)).thenReturn(foundTeacher);
    boolean returnValue = service.delete(urlId);
    verify(repository, times(1)).existsByUrlId(urlId);
    verify(cache, times(1)).remove(urlId);
    verify(repository, times(1)).findByUrlId(urlId);
    verify(repository, times(1)).delete(foundTeacher);
    assertTrue(returnValue);
  }

  @Test
  void delete_DoesNotExists() {
    String urlId = "l-podenok";
    when(repository.existsByUrlId(urlId)).thenReturn(false);
    boolean returnValue = service.delete(urlId);
    verify(repository, times(1)).existsByUrlId(urlId);
    verify(cache, never()).remove(any());
    verify(repository, never()).findByUrlId(any());
    verify(repository, never()).delete(any());
    assertFalse(returnValue);
  }
}
