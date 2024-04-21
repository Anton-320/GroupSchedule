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
    verify(cache, times(1)).get(id);
    verify(repository, times(1)).findById(id);
    assertNull(auditorium);
  }

  @Test
  void createTest_NotExists() {
    String auditorium = "505-5 к.";
    when(repository.existsByName(auditorium)).thenReturn(false);
    when(repository.save(any())).thenReturn(new Auditorium(2L, "505-5 к.", any()));
    String finalResult = service.create(auditorium);
    verify(repository, times(1)).save(any());
    verify(cache, times(1)).put(any(), any());
    assertEquals(auditorium, finalResult);
  }

  @Test
  void createTest_Exists() {
    String auditorium = "505-5 к.";
    when(repository.existsByName(auditorium)).thenReturn(true);
    String finalResult = service.create(auditorium);
    verify(repository, never()).save(any());
    verify(cache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void updateTest_Exists() {
    Long id = 5L;
    String initialValue = "505-5 к.";
    String newValue = "514-5 к.";
    Auditorium auditorium = new Auditorium(125L, initialValue, any());
    when(repository.findById(id)).
        thenReturn(Optional.of(auditorium));
    when(repository.save(new Auditorium(id, newValue, any()))).thenReturn(new Auditorium(125L, newValue, any()));
    String finalResult = service.update(id, initialValue);
    verify(repository, times(1)).findById(id);
    verify(repository, times(1)).save(any());
    verify(cache, times(1)).put(any(), any());
    assertEquals(newValue, finalResult);
  }

  @Test
  void updateTest_NotExists() {
    String initialValue = "505-5 к.";
    when(repository.findById(any())).
        thenReturn(Optional.empty());
    String finalResult = service.update(any(), initialValue);
    verify(repository, times(1)).findById(any());
    verify(repository, never()).save(any());
    verify(cache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void deleteTest_Exists() {
    Long id = 5L;
    Auditorium auditoriumTmp = new Auditorium(id, "505-5 к.", new ArrayList<>());
    when(repository.findById(anyLong())).thenReturn(Optional.of(auditoriumTmp));
    boolean result = service.delete(id);
    verify(repository, times(1)).findById(anyLong());
    verify(repository, times(1)).delete(any());
    verify(cache, times(1)).remove(any());
    assertTrue(result);
  }

  @Test
  void deleteTest_DoesNotExists() {
    Long id = 5L;
    Auditorium auditoriumTmp = new Auditorium(id, "505-5 к.", new ArrayList<>());
    when(repository.findById(anyLong())).thenReturn(Optional.empty());
    boolean result = service.delete(id);
    verify(repository, times(1)).findById(anyLong());
    verify(repository, never()).delete(any());
    verify(cache, never()).remove(any());
    assertFalse(result);
  }
}


