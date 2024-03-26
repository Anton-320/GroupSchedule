package com.byshnev.groupschedule.service.changes;

import com.byshnev.groupschedule.cache.GroupCache;
import com.byshnev.groupschedule.cache.ScheduleChangesCache;
import com.byshnev.groupschedule.model.entity.StudentGroup;
import com.byshnev.groupschedule.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {
	private GroupRepository repository;
	private GroupCache groupCache;
	private ScheduleChangesCache lessonCache;

	public List<Integer> getAllGroups() {
		return repository.findAll().stream()
				.map(StudentGroup::getGroupNum)
				.collect(Collectors.toList());
	}

	public Integer getById(Integer id) {
		StudentGroup tmp = groupCache.get(id).orElse(null);
		if (tmp != null)
			return tmp.getGroupNum();
		else if ((tmp = repository.findById(id).orElse(null)) != null) {
			groupCache.put(id, tmp);
			return tmp.getGroupNum();
		}
		return null;
	}

	public Integer add(Integer groupNum) {
		if (repository.findByGroupNum(groupNum) == null) {
			StudentGroup tmp = repository.save(new StudentGroup(groupNum));
			groupCache.put(tmp.getId(), tmp);
			return tmp.getGroupNum();
		}
		else return null;
	}

	public Integer update(Integer id, Integer groupNum) {
		StudentGroup tmp = repository.findById(id).orElse(null);
		if (tmp != null) {
			tmp.setGroupNum(groupNum);
			groupCache.put(id, tmp);
			return repository.save(tmp).getGroupNum();
		}
		else return null;
	}

	public boolean delete (Integer id) {
		if (repository.existsById(id)) {
			groupCache.remove(id);
			for (var lesson : repository.findById(id).get().getLessons())
				lessonCache.remove(lesson.getId());
			repository.deleteById(id);
			return true;
		}
		else return false;
	}

}
