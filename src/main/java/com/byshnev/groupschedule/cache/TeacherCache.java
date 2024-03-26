package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.Teacher;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TeacherCache extends Cache<String, Teacher> {

}
