package com.sample.mvcApp.feature.mypage.task.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupUseCase;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupGateway;

@Service
public class TaskGroupService implements TaskGroupUseCase {
	
	@Autowired
	TaskGroupGateway taskGroupGateway;

	@Override
	@Transactional
	public void create(TaskGroupCreateInput input) {
		
		var TaskGroupId = new TaskGroupId(input.taskGroupId(), input.workYmd());
		var title = new Title(input.title());
		String description = input.description();
		String taskTypeCode = input.taskTypeCode();
		Priority priority = Priority.parsePriority(input.priority());
		var plannedTime = new TimeSlot(input.plannedStartTime(), input.plannedEndTime());
		
		TaskGroup taskGroup = TaskGroup.newCreate(
				  TaskGroupId
				, title
				, description
				, taskTypeCode
				, priority
				, plannedTime);
		
		taskGroupGateway.save(taskGroup);
		
	}

}
