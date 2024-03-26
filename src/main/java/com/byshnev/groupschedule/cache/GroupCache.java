package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.entity.StudentGroup;
import org.springframework.stereotype.Component;

@Component
public class GroupCache extends Cache<Integer, StudentGroup> {
}
