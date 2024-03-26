package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class ScheduleChangesCache extends Cache<Long, Lesson> {

}
