package com.sample.mvcApp.feature.mypage.task.adapter.web.helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sample.mvcApp.common.util.IdUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.web.form.TaskGroupCreateForm;
import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskSummaryDetailItemView;
import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskSummaryItemView;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupWeekOutput;

public class TaskGroupWebHelper {
	
	public static TaskGroupCreateInput parseToTaskGroupCreateInput(TaskGroupCreateForm form) {
		
		return new TaskGroupCreateInput(
				//UUID生成
				  IdUtil.generateId()
				, form.getTargetYmd()
				, form.getTitle()
				, form.getExplanation()
				, form.getTaskKind()
				, form.getPriority()
				, form.getPlannedStart()
				, form.getPlannedEnd()
				);
	}
	
	public static TaskSummaryItemView parseToTaskSummaryItemView(TaskGroupWeekOutput output) {
		
		Map<LocalDate, List<TaskSummaryDetailItemView>> map = output.taskGroupByWeekMap()
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
								e -> e.getKey(),
								e -> e.getValue()
										.stream()
										.map(og -> new TaskSummaryDetailItemView().builder()
												.groupId(og.groupId())
												.title(og.title())
												.startTime(og.plannedStartTime())
												.endTime(og.plannedEndTime())
												.priority(og.priority())
												.taskKind(og.taskTypeCode())
												.build()
												)
										.toList()
								)
						);
		
		return  TaskSummaryItemView.builder()
				.TaskSummaryItemViewMaps(map)
				.build();
	}

}
