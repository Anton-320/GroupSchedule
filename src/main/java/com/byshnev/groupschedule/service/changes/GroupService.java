package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.cache.GroupCache;
import com.byshnev.groupschedule.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {
	private GroupRepository groupRepository;
	private GroupCache groupCache;
	private ScheduleChangesCache lessonCache;
	private LessonRepository lessonRepository;

	public List<Integer> getAllGroups() {
		return groupRepository.findAll().stream()
				.map(StudentGroup::getGroupNum)
				.collect(Collectors.toList());
	}

	public Integer getById(Integer groupNum) {
		StudentGroup tmp = groupCache.get(groupNum).orElse(null);
		if (tmp != null)
			return tmp.getGroupNum();
		else if ((tmp = groupRepository.findById(groupNum).orElse(null)) != null) {
			groupCache.put(groupNum, tmp);
			return tmp.getGroupNum();
		}
		return null;
	}

	public Integer add(StudentGroup group) {
		if (groupRepository.findByGroupNum(group.getGroupNum()) == null) {
			StudentGroup tmp = groupRepository.save(new StudentGroup(
					group.getGroupNum(),
					group.getStudentsAmount()));
			groupCache.put(tmp.getGroupNum(), tmp);
			return tmp.getGroupNum();
		}
		else return null;
	}

	public Integer update(Integer groupNum, StudentGroup group) {
		StudentGroup tmp = groupRepository.findById(groupNum).orElse(null);
		if (tmp != null) {
			tmp.setGroupNum(group.getGroupNum());
			tmp.setStudentsAmount(group.getStudentsAmount());
			groupCache.put(groupNum, tmp);
			return groupRepository.save(tmp).getGroupNum();
		}
		else return null;
	}

	public boolean delete(Integer groupNum) {
		if (groupRepository.existsById(groupNum)) {
			for (var lesson : groupRepository.findById(groupNum).get().getLessons()) {
				lessonCache.remove(lesson.getId());
			}
			groupCache.remove(groupNum);
			lessonRepository.deleteByGroupGroupNum(groupNum);
			groupRepository.deleteById(groupNum);
			return true;
		}
		else return false;
	}

}
