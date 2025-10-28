package com.sample.mvcApp.feature.mypage.task.adapter.db.helper;

import java.util.Optional;

import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupKeyDto;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;

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
	
	/**
	 * TaskGroupDto から TaskGroup エンティティへ変換
	 * 
	 * @param dto	タスクグループDTO
	 * @return		タスクグループエンティティ
	 */
	public static TaskGroup parseToTaskGroup (TaskGroupDto dto) {
		return new TaskGroup(
				 new TaskGroupId(
						dto.getId().getTaskGroupId()
						, dto.getId().getWorkYmd()
						)
				,new Title(dto.getTitle())
				,dto.getDescription()
				,dto.getTaskTypeCode()
				,Priority.parsePriority(dto.getPriority())
				,new TimeSlot(dto.getPlannedStartTime(), dto.getPlannedEndTime())
				,new TimeSlot(dto.getActualStartTime(), dto.getActualEndTime())
				,TaskStatus.fromLabel(dto.getStatus())
				,dto.isTemplate()
				);
	}
}
