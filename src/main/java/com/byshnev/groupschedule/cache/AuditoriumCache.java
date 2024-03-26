package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.Auditorium;
import org.springframework.stereotype.Component;

@Component
public class AuditoriumCache extends Cache<Long, Auditorium> {
}