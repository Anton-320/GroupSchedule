package com.byshnev.groupschedule.api;

import com.byshnev.groupschedule.model.dto.LessonDto;
import com.byshnev.groupschedule.model.dto.TeacherDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BsuirApiService {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String HTTPS_URL_BSUIR_SRCH = "https://iis.bsuir.by/api/v1/schedule?studentGroup={groupNumber}";
	private static final String[] daysOfWeek = {
			"Понедельник",
			"Вторник",
			"Среда",
			"Четверг",
			"Пятница",
			"Суббота"
	};

	//Возвращает 2 числа
	//первое - номер недели, второе - порядковый номер дня недели (понедельник == 1, и т.д.)
	//Отсчёт ведётся с 1 января 2024 года (понедельник)
	private int[] defineWeekNumber(LocalDate date) {
		int[] res = new int[2];
		LocalDate tmpDate = LocalDate.parse("01-01-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		int weekNum = 3;	//1 Января - 3-я учебная неделя
		for (; tmpDate.isBefore(date); weekNum += 4) {
			tmpDate = tmpDate.plusDays(28);    //проматываем циклически по 4 недели, пока по дате не перелёт
		}
		if (!tmpDate.isEqual(date)) {
			for (; tmpDate.isAfter(date); weekNum -= 1)
				tmpDate = tmpDate.minusDays(7);
		}
		res[0] = (weekNum % 4) > 0 ? weekNum % 4 : 4;
		//Вычисление дня недели
		for (res[1] = 1; !tmpDate.isEqual(date); res[1]++)
			tmpDate = tmpDate.plusDays(1);
		return res;
	}

	public List<LessonDto> getScheduleFromBsuirApi(Integer groupNum, LocalDate date) throws JsonProcessingException {
		int[] dateInfo = defineWeekNumber(date);	//номер недели и день недели
		if (dateInfo[1] > 6)
			return Collections.emptyList();

		String jsonResponseStr = restTemplate.getForObject(HTTPS_URL_BSUIR_SRCH, String.class, groupNum.toString());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(jsonResponseStr);
		JsonNode scheduleArrayNode = root.get("schedules").get(daysOfWeek[dateInfo[1] - 1]);
		List<LessonDto> result = new ArrayList<>();
		if (scheduleArrayNode.size() == 0)
			return Collections.emptyList();
		for (var subject : scheduleArrayNode) {
			for (var weekNumbersIter : subject.get("weekNumber")) {
				if (weekNumbersIter.asInt() == dateInfo[0]) {
					result.add(new LessonDto(
							subject.get("subject").asText(null),
							subject.get("subjectFullName").asText(null),
							subject.get("startLessonTime").asText(null),
							subject.get("endLessonTime").asText(null),
							subject.get("note").asText(null),
							subject.get("lessonTypeAbbrev").asText(null),
							StreamSupport.stream(subject.get("auditories").spliterator(), false)
									.map(JsonNode::asText)	//method reference (instead of lambda)
									.collect(Collectors.toList()),
							subject.get("numSubgroup").asInt(0),
							StreamSupport.stream(subject.get("employees").spliterator(), false)
									.map(teacherNode -> new TeacherDto(
												 teacherNode.get("urlId").asText(null),
												 teacherNode.get("firstName").asText(null),
												 teacherNode.get("lastName").asText(null),
												 teacherNode.get("middleName").asText(null),
												 teacherNode.get("degree").asText(null),
												 teacherNode.get("email").asText(null)
										 )
									)
									.collect(Collectors.toList())
					));
					break;
				}
			}
		}
		return result;
	}

}
