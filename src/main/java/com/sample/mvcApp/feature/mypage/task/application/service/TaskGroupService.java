package com.sample.mvcApp.feature.mypage.task.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.mvcApp.common.util.DateUtil;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupSummaryOutput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupWeekOutput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupUseCase;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.DayTaskGroupCollection;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.TaskGroupCollectionMap;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.WeekRange;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupGateway;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupQuery;

@Service
public class TaskGroupService implements TaskGroupUseCase {
	
	@Autowired
	TaskGroupGateway taskGroupGateway;
	
	@Autowired
	TaskGroupQuery taskGroupQuery;

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

	@Override
	@Transactional(readOnly = true)
	public TaskGroupWeekOutput getTaskGroupWeekRange() {
		
		LocalDate nowOnMonday = DateUtil.getThisWeekMonday();
		var range = WeekRange.of(nowOnMonday);
		TaskGroupCollectionMap map = taskGroupQuery.getTaskGroupWeekRange(range);
		TaskGroupWeekOutput output = this.toTaskGroupWeekOutput(map);
		return output;
		
	}
	
	private TaskGroupWeekOutput toTaskGroupWeekOutput(TaskGroupCollectionMap map) {
		
		Map<LocalDate, List<TaskGroupSummaryOutput>> byDate = map.getByDate().entrySet().stream()
				.collect(Collectors.toMap(
						e -> e.getKey(),
						e -> toTaskGroupSummaryOutput(e.getValue())
						));
		return null;
	}
	
	private List<TaskGroupSummaryOutput> toTaskGroupSummaryOutput(DayTaskGroupCollection collection) {
		return collection.getTaskGroups().stream()
				.map(g -> new TaskGroupSummaryOutput(
						g.id().toString(),
						g.title().toString(),
						g.plannedTime().startTime().toString(),
						g.plannedTime().endTime().toString(),
						g.priority().getLabel(),
						g.taskTypeCode()
						))
				.collect(Collectors.toList());
	}
	
	

}
