package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.AuditoriumCache;
import com.byshnev.groupschedule.model.entity.Auditorium;
import com.byshnev.groupschedule.repository.AuditoriumRepository;
import com.byshnev.groupschedule.service.changes.AuditoriumService;
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
public class AuditoriumServiceTest {

  @InjectMocks
  private AuditoriumService service;

  @Mock
  private AuditoriumRepository repository;

  @Mock
  private AuditoriumCache cache;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll() {
    Auditorium auditorium = new Auditorium("311-4 к.");
    List<Auditorium> auditoriumList = new ArrayList<>();
    auditoriumList.add(new Auditorium("311-4 к."));
    auditoriumList.add(new Auditorium("514-5 к."));

    when(repository.findAll()).thenReturn(auditoriumList);

    List<String> finalResponse = service.getAll();
    assertEquals(2, finalResponse.size());
  }

  @Test
  void getByIdTest_ExistsInCache() {
    Long id = anyLong();
    when(cache.get(id)).thenReturn(Optional.of("505-5 к."));
    String finalResult = service.getById(id);
    assertNotNull(finalResult);
  }

  @Test
  void getByIdTest_ExistsInRepository() {
    Long id = 125L;
    when(cache.get(id)).thenReturn(Optional.empty());
    when(repository.findById(id)).thenReturn(Optional.of(new Auditorium("514-5 к.")));
    String auditorium = service.getById(id);
    verify(cache, times(1)).get(id);
    verify(repository, times(1)).findById(any());
    verify(cache, times(1)).put(eq(id), any());
    assertNotNull(auditorium);
  }

  @Test
  void getByIdTest_NotExists() {
    Long id = 125L;
    when(cache.get(id)).thenReturn(Optional.empty());
    when(repository.findById(id)).thenReturn(Optional.empty());
    String auditorium = service.getById(id);
    verify(repository, times(1)).findById(any());
    verify(cache, times(1)).put(id, auditorium);
    assertNull(auditorium);
  }
}

