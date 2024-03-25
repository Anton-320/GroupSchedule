package com.byshnev.groupschedule.service.changes;

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

	public List<Integer> getAllGroups() {
		return repository.findAll().stream()
				.map(StudentGroup::getGroupNum)
				.collect(Collectors.toList());
	}

	public Integer getById(Integer groupNum) {
		return repository.findByGroupNum(groupNum).getGroupNum();
	}

	public Integer add(Integer groupNum) {
		if (repository.findByGroupNum(groupNum) == null)
			return repository.save(new StudentGroup(groupNum)).getGroupNum();
		else return null;
	}

	public Integer update(Integer id, Integer groupNum) {
		StudentGroup tmp = repository.findById(id).orElse(null);
		if (tmp != null) {
			tmp.setGroupNum(groupNum);
			return repository.save(tmp).getGroupNum();
		}
		else return null;
	}

	public boolean delete (Integer id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}
		else return false;
	}

}
