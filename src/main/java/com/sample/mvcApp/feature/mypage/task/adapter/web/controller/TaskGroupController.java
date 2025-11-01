package com.sample.mvcApp.feature.mypage.task.adapter.web.controller;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sample.mvcApp.feature.mypage.task.adapter.web.form.TaskGroupCreateForm;
import com.sample.mvcApp.feature.mypage.task.adapter.web.helper.TaskGroupWebHelper;
import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskSummaryItemView;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupWeekOutput;
import com.sample.mvcApp.feature.mypage.task.application.usecase.TaskGroupUseCase;

/*
 * タスクボード一覧Controller
 */
@Controller
public class TaskGroupController {
	
	@Autowired
	TaskGroupUseCase taskGroupUseCase;

	/**
	 * 初期表示
	 * @param model
	 * @return
	 */
	@GetMapping("task/list")
	public String Initialize(Model model) {
		
		TaskGroupWeekOutput output = taskGroupUseCase.getTaskGroupWeekRange();

		TaskSummaryItemView view = TaskGroupWebHelper.parseToTaskSummaryItemView(output);
		model.addAttribute("taskSummaryMaps",new TreeMap<>(view.getTaskSummaryItemViewMaps()));
		model.addAttribute("taskGroupCreateForm", new TaskGroupCreateForm());
		return "task/list";
	}
	
	@PostMapping("task/new")
	public String PostCreateTaskGroup(TaskGroupCreateForm form) {
		
		TaskGroupCreateInput input = TaskGroupWebHelper.parseToTaskGroupCreateInput(form);
		taskGroupUseCase.create(input);
		return "redirect:/mypage/task/list";
	}
	

}
