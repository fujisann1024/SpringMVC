package com.sample.mvcApp.feature.mypage.task.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupDetailInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupDetailOutput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupDetailUseCase;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupQuery;

@Service
public class TaskGroupDetailService implements TaskGroupDetailUseCase {

	@Autowired
	TaskGroupQuery taskGroupQuery;
	
	@Override
	public TaskGroupDetailOutput getTaskGroupDetail(TaskGroupDetailInput input) {
		
		TaskGroupId id = new TaskGroupId(input.groupId(), input.workYmd());
		TaskGroup taskGroup = taskGroupQuery.getTaskGroupById(id);
		
		return new TaskGroupDetailOutput(
				  taskGroup.id().groupId()
				, taskGroup.id().workYmd()
				, taskGroup.title().value()
				, taskGroup.description().orElse("")
				, taskGroup.taskTypeCode().orElse("")
				, taskGroup.priority().getLabel()
				, taskGroup.plannedTime().map(TimeSlot::getStartTimeHHmm).orElse("")
				, taskGroup.plannedTime().map(TimeSlot::getEndTimeHHmm).orElse("")
				, taskGroup.actualTime().map(TimeSlot::getStartTimeHHmm).orElse("")
				, taskGroup.actualTime().map(TimeSlot::getEndTimeHHmm).orElse("")
				, taskGroup.status().getLabel()
				, taskGroup.template()
				);
	}

}
