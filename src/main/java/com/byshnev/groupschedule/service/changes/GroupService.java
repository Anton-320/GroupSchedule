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

  public GroupDto getGroupByNumber(Integer groupNumber) {
    GroupDto tmpDto = groupCache.get(groupNumber).orElse(null);
    if (tmpDto != null) {
      return tmpDto;
    }
    StudentGroup tmp;
    tmp = groupRepository.findById(groupNumber).orElse(null);
    if (tmp != null) {
      tmpDto = GroupUtility.convertToDto(tmp);
      groupCache.put(groupNumber, tmpDto);
      return tmpDto;
    }
    return null;
  }

  @Transactional
  public GroupDto add(GroupDto group) {
    if (!groupRepository.existsById(group.getGroupNumber())) {
      groupCache.put(group.getGroupNumber(), group);
      return GroupUtility.convertToDto(
          groupRepository.save(GroupUtility.createEntityWithoutLink(group)));
    } else {
      return null;
    }
  }

  @Transactional
  public GroupDto update(Integer groupNumber, GroupDto group) {
    StudentGroup tmp = groupRepository.findById(groupNumber).orElse(null);
    if (tmp != null) {
      tmp.setStudentsAmount(group.getStudentsAmount());
      groupCache.put(groupNumber, group);
      groupRepository.flush();
      return group;
    } else {
      return null;
    }
  }

  @Transactional
  public boolean delete(Integer groupNumber) {
    StudentGroup tmp = groupRepository.findById(groupNumber).orElse(null);
    if (tmp == null) {
      return false;
    }
    for (var lesson : tmp.getLessons()) {
      lessonCache.remove(lesson.getId());
    }
    groupCache.remove(groupNumber);
    lessonService.deleteByGroup(groupNumber);
    groupRepository.deleteById(groupNumber);
    return true;
  }
}
