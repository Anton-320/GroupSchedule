package com.byshnev.groupschedule.service;

import com.byshnev.groupschedule.components.cache.GroupCache;
import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.service.changes.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
  @InjectMocks
  private GroupService service;

  @Mock
  private GroupRepository repository;

  @Mock
  private GroupCache cache;

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

  }

}
