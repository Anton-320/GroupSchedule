package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.Auditorium;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AuditoriumCache extends Cache<Long, Auditorium> {
}