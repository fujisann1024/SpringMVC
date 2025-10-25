package com.sample.mvcApp.feature.mypage.task.adapter.web.helper;

import com.sample.mvcApp.common.util.IdUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.web.form.TaskGroupCreateForm;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;

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

}
