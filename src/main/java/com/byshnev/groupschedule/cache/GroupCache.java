package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.StudentGroup;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GroupCache extends Cache<Integer, StudentGroup> {
}
