package com.byshnev.groupschedule.service.utility;

import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;

public class GroupUtility {

	private GroupUtility() {

	}

	public static GroupDto convertToDto(StudentGroup group) {
		return new GroupDto(group.getGroupNum(), group.getStudentsAmount());
	}

	public static StudentGroup createEntityWithoutLink(GroupDto dto) {
		return new StudentGroup(dto.getGroupNum(), dto.getStudentsAmount());
	}
}

