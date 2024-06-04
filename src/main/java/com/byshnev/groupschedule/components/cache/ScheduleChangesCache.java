package com.byshnev.groupschedule.components.cache;

import com.byshnev.groupschedule.model.dto.ChangeDto;
import org.springframework.stereotype.Component;

@Component
public class ScheduleChangesCache extends Cache<Long, ChangeDto> {

}
