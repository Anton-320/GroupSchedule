package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.components.cache.GroupCache;
import com.byshnev.groupschedule.components.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.dto.GroupDto;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.service.utility.GroupUtility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {
	private GroupRepository groupRepository;
	private GroupCache groupCache;
	private ScheduleChangesCache lessonCache;
	private LessonService lessonService;

	@Transactional
	public List<GroupDto> getAllGroups() {
		return groupRepository.findAll().stream()
				.map(GroupUtility::convertToDto)
				.toList();
	}

	public GroupDto getGroupByNum(Integer groupNum) {
		GroupDto tmpDto = groupCache.get(groupNum).orElse(null);
		StudentGroup tmp;
		if (tmpDto != null)
			return tmpDto;
		else if ((tmp = groupRepository.findById(groupNum).orElse(null)) != null) {
			tmpDto = GroupUtility.convertToDto(tmp);
			groupCache.put(groupNum, tmpDto);
			return tmpDto;
		}
		return null;
	}

	@Transactional
	public GroupDto add(GroupDto group) {
		if (groupRepository.findByGroupNum(group.getGroupNum()) == null) {
			groupCache.put(group.getGroupNum(), group);
			return GroupUtility.convertToDto(
					groupRepository.save(GroupUtility.createEntityWithoutLink(group)));
		}
		else return null;
	}

	@Transactional
	public GroupDto update(Integer groupNum, GroupDto group) {
		StudentGroup tmp = groupRepository.findById(groupNum).orElse(null);
		if (tmp != null) {
			tmp.setGroupNum(group.getGroupNum());
			tmp.setStudentsAmount(group.getStudentsAmount());
			groupCache.put(groupNum, group);
			groupRepository.flush();
			return group;
		}
		else return null;
	}

	@Transactional
	public boolean delete(Integer groupNum) {
		StudentGroup tmp = groupRepository.findById(groupNum).orElse(null);
		if (tmp == null) {
			return false;
		}
		for (var lesson : tmp.getLessons()) {
			lessonCache.remove(lesson.getId());
		}
		groupCache.remove(groupNum);
		lessonService.deleteByGroup(groupNum);
		groupRepository.deleteById(groupNum);
		return true;
	}

}
