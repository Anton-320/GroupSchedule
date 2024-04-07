package com.byshnev.groupschedule.components.cache;

import com.byshnev.groupschedule.model.dto.LessonDto;
import org.springframework.stereotype.Component;

@Component
public class ScheduleChangesCache extends Cache<Long, LessonDto> {

}
