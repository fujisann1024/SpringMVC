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
				.description(entity.description().orElse(null))
				.taskTypeCode(entity.taskTypeCode().orElse(null))
				.priority(entity.priority().getLabel())
				.plannedStartTime(entity.plannedTime().orElse(null).startTime())
				.plannedEndTime(entity.plannedTime().orElse(null).endTime())
				.actualStartTime(entity.actualTime().orElse(null).startTime())
				.actualEndTime(entity.actualTime().orElse(null).endTime())
				.status(entity.status().getLabel())
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
				,Optional.ofNullable(dto.getDescription())  
				,Optional.ofNullable(dto.getTaskTypeCode())
				,Priority.parsePriority(dto.getPriority())
				,Optional.ofNullable(new TimeSlot(dto.getPlannedStartTime(), dto.getPlannedEndTime()))
				,Optional.ofNullable(new TimeSlot(dto.getActualStartTime(), dto.getActualEndTime()))
				,TaskStatus.fromLabel(dto.getStatus())
				,dto.isTemplate()
				);
	}
}
