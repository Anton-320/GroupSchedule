package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.Lesson;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ScheduleChangesCache extends Cache<Long, Lesson> {

}
