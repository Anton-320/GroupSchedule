package com.byshnev.groupschedule.components.cache;

import com.byshnev.groupschedule.model.dto.LessonDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleGettingCache extends Cache<String, List<LessonDto>> {
}
