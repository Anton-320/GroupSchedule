package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.GroupCache;
import com.byshnev.groupschedule.components.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.service.changes.GroupService;
import com.byshnev.groupschedule.service.changes.LessonService;
import com.byshnev.groupschedule.service.utility.GroupUtility;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
  @InjectMocks
  private GroupService service;

  @Mock
  private GroupRepository repository;

  @Mock
  private ScheduleChangesCache lessonCache;

  @Mock
  private LessonService lessonService;

  @Mock
  private GroupCache groupCache;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll() {
    when(repository.findAll()).thenReturn(List.of(
        new StudentGroup(250501, 24),
        new StudentGroup(251002, 25)));
    List<GroupDto> finalResult = service.getAllGroups();
    assertEquals(finalResult.size(), 2);
  }

  @Test
  void getById_ExistsInCache() {
    Integer id = 250501;
    GroupDto gotDto = new GroupDto(250501, 24);
    when(groupCache.get(id)).thenReturn(Optional.of(gotDto));
    GroupDto finalResult = service.getGroupByNumber(id);
    verify(groupCache, times(1)).get(id);
    verify(repository, never()).findById(any());
    verify(groupCache, never()).put(any(), any());
    assertEquals(finalResult, gotDto);
  }

  @Test
  void getById_ExistsInRepository() {
    Integer id = 250501;
    StudentGroup gotEntity = new StudentGroup(250501, 24, new ArrayList<>());
    when(groupCache.get(id)).thenReturn(Optional.empty());
    when(repository.findById(id)).thenReturn(Optional.of(gotEntity));
    GroupDto finalResult = service.getGroupByNumber(id);
    verify(groupCache, times(1)).get(id);
    verify(repository, times(1)).findById(id);
    verify(groupCache, times(1)).put(any(), any());
    assertEquals(finalResult.getGroupNumber(), gotEntity.getGroupNumber());
    assertEquals(finalResult.getStudentsAmount(), gotEntity.getStudentsAmount());
  }

  @Test
  void getById_DoesNotExists() {
    Integer id = 250501;
    when(groupCache.get(id)).thenReturn(Optional.empty());
    when(repository.findById(id)).thenReturn(Optional.empty());
    GroupDto finalResult = service.getGroupByNumber(id);
    verify(groupCache, times(1)).get(id);
    verify(repository, times(1)).findById(id);
    verify(groupCache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void add_DoesNotExists() {
    GroupDto dto = new GroupDto(250501, 24);
    Integer id = dto.getGroupNumber();
    when(repository.existsById(id)).thenReturn(false);
    when(repository.save(any())).thenReturn(GroupUtility.createEntityWithoutLink(dto));
    GroupDto finalResult = service.add(dto);
    verify(repository, times(1)).existsById(id);
    verify(groupCache, times(1)).put(id, dto);
    assertEquals(finalResult, dto);
  }

  @Test
  void add_Exists() {
    GroupDto dto = new GroupDto(250501, 24);
    Integer id = dto.getGroupNumber();
    when(repository.existsById(id)).thenReturn(true);
    GroupDto finalResult = service.add(dto);
    verify(repository, times(1)).existsById(id);
    verify(groupCache, never()).put(any(), any());
    assertNull(finalResult);
  }

  @Test
  void update() {
  }

  @Test
  void delete() {
  }
}

