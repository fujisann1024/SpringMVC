package com.sample.mvcApp.feature.mypage.task.adapter.web.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.mvcApp.feature.mypage.task.adapter.web.helper.TaskGroupDetailWebHelper;
import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskDetailItemView;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupDetailInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupDetailOutput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupDetailUseCase;

/*
 * タスクボード詳細Controller
 */
@Controller
public class TaskGroupDetailController {

	@Autowired
	TaskGroupDetailUseCase taskGroupDetailUseCase;

	/**
	 * 詳細表示
	 */
	@GetMapping("task/detail")
	public String detail(
			@RequestParam("groupId") String groupId,
			@RequestParam("workYmd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workYmd,
			Model model) {

		TaskGroupDetailInput input = TaskGroupDetailWebHelper.parseToTaskGroupDetailInput(groupId, workYmd);
		TaskGroupDetailOutput output = taskGroupDetailUseCase.getTaskGroupDetail(input);
		TaskDetailItemView view = TaskGroupDetailWebHelper.parseToTaskDetailItemView(output);
		model.addAttribute("taskDetail", view);
		return "task/detail";
	}
}
