package com.byshnev.groupschedule.cache;

import com.byshnev.groupschedule.model.dto.GroupDto;
import org.springframework.stereotype.Component;

@Component
public class GroupCache extends Cache<Integer, GroupDto> {
}
