package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.cache.GroupCache;
import com.byshnev.groupschedule.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.repository.GroupRepository;
import com.byshnev.groupschedule.repository.LessonRepository;
import jakarta.transaction.Transactional;
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
	private LessonService lessonService;

	@Transactional
	public List<Integer> getAllGroups() {
		return groupRepository.findAll().stream()
				.map(StudentGroup::getGroupNum)
				.collect(Collectors.toList());
	}

	@Transactional
	public StudentGroup getByNum(Integer groupNum) {
		StudentGroup tmp = groupCache.get(groupNum).orElse(null);
		if (tmp != null)
			return tmp;
		else if ((tmp = groupRepository.findById(groupNum).orElse(null)) != null) {
			groupCache.put(groupNum, tmp);
			return tmp;
		}
		return null;
	}

	@Transactional
	public StudentGroup add(StudentGroup group) {
		if (groupRepository.findByGroupNum(group.getGroupNum()) == null) {
			StudentGroup tmp = groupRepository.save(new StudentGroup(
					group.getGroupNum(),
					group.getStudentsAmount()));
			groupCache.put(tmp.getGroupNum(), tmp);
			return tmp;
		}
		else return null;
	}

	@Transactional
	public StudentGroup update(Integer groupNum, StudentGroup group) {
		StudentGroup tmp = groupRepository.findById(groupNum).orElse(null);
		if (tmp != null) {
			tmp.setGroupNum(group.getGroupNum());
			tmp.setStudentsAmount(group.getStudentsAmount());
			groupCache.put(groupNum, tmp);
			return groupRepository.save(tmp);
		}
		else return null;
	}

	@Transactional
	public boolean delete(Integer groupNum) {
		if (!groupRepository.existsById(groupNum)) {
			return false;
		}
		for (var lesson : groupRepository.findById(groupNum).get().getLessons()) {
			lessonCache.remove(lesson.getId());
		}
		groupCache.remove(groupNum);
		lessonService.deleteByGroup(groupNum);
		groupRepository.deleteById(groupNum);
		return true;
	}

}
