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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceClass {
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
  void getByUrlId() {

  }

  @Test
  void add() {
  }

  @Test
  void update() {
  }

  @Test
  void delete() {
  }
}
