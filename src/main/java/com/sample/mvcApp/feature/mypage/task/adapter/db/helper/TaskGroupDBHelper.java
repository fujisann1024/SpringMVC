package com.sample.mvcApp.feature.mypage.task.adapter.db.helper;

import java.util.Optional;

import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupKeyDto;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;

public class TaskGroupDBHelper {

	public static TaskGroupDto parseToTaskGroupDto (TaskGroup entity) {
		return TaskGroupDto.builder()
				.id(TaskGroupKeyDto.builder()
						.taskGroupId(entity.id().groupId())
						.workYmd(entity.id().workYmd())
						.build())
				.title(entity.title().value())
				.description(entity.description())
				.taskTypeCode(entity.taskTypeCode())
				.priority(entity.priority().getLabel())
				.plannedStartTime(Optional.ofNullable(entity.plannedTime())
										  .map(TimeSlot::startTime)
										  .orElse(null))
				.plannedEndTime(Optional.ofNullable(entity.plannedTime())
										.map(TimeSlot::endTime)
										.orElse(null))
				.actualStartTime(Optional.ofNullable(entity.actualTime())
										.map(TimeSlot::startTime)
										.orElse(null))
				.actualEndTime(Optional.ofNullable(entity.actualTime())
									  .map(TimeSlot::endTime)
									  .orElse(null))
				.status(Optional.ofNullable(entity.status())
						        .map(TaskStatus::getLabel)
						        .orElse(null))
				.isTemplate(entity.template())
				.build();
	}
}
