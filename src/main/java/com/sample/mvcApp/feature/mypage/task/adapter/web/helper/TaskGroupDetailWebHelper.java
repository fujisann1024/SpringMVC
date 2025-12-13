package com.sample.mvcApp.feature.mypage.task.adapter.web.helper;

import java.time.LocalDate;

import com.sample.mvcApp.common.util.DateUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskDetailItemView;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupDetailInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupDetailOutput;

public class TaskGroupDetailWebHelper {
	
	public static TaskGroupDetailInput parseToTaskGroupDetailInput(String groupId, LocalDate workYmd) {
		
		return new TaskGroupDetailInput(
				  groupId
				, workYmd
				);
	}

	public static TaskDetailItemView parseToTaskDetailItemView(TaskGroupDetailOutput output) {
		String plannedTime = output.plannedStartTime() + " - " + output.plannedEndTime();
		String actualTime = output.actualStartTime() + " - " + output.actualEndTime();
		return TaskDetailItemView.builder()
				.groupId(output.groupId())
				.workDate(DateUtil.formatLocalDate(output.workYmd(),"yyyy/MM/dd"))	
				.title(output.title())
				.description(output.description() == "" ? "未入力" : output.description())
				.taskKind(output.taskTypeCode())
				.priority(output.priority())
				.plannedTime(plannedTime)
				.actualTime(actualTime)
				.status(output.status())
				.template(output.template())
				.build();
	}

}
