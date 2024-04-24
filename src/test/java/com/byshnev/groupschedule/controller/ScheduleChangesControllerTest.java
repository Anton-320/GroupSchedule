package com.byshnev.groupschedule.controller;

import com.byshnev.groupschedule.service.changes.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduleChangesControllerTest {

  @InjectMocks
  private ScheduleChangesController controller;

  @Mock
  private LessonService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }



}
