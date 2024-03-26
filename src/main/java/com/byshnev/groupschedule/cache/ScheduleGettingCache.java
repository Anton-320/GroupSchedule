package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.dto.LessonDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Component
public class ScheduleGettingCache extends Cache<Integer, Cache<LocalDate, List<LessonDto>>> {
}
