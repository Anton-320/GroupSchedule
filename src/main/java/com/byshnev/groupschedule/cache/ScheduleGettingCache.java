package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.dto.LessonDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Component
public class ScheduleGettingCache extends Cache<Integer, HashMap<LocalDate, List<LessonDto>>> {
}
