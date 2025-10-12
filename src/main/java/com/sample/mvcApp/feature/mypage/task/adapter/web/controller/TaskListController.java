package com.sample.mvcApp.feature.mypage.task.adapter.web.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sample.mvcApp.feature.mypage.task.adapter.web.form.TaskGroupCreateForm;
import com.sample.mvcApp.feature.mypage.task.adapter.web.itemView.TaskSummaryItemView;

/*
 * タスクボード一覧Controller
 */
@Controller
public class TaskListController {

	/**
	 * 初期表示
	 * @param model
	 * @return
	 */
	@GetMapping("task/list")
	public String Initialize(Model model) {

		Map<String, List<TaskSummaryItemView>> TaskSummaryItemViewMaps = Map.ofEntries(
				Map.entry(
						"10/11",
						List.of(
								new TaskSummaryItemView().builder()
								.groupId("1111")
								.title("朝活動")
								.startTime("7:00:00")
								.endTime("9:00:00")
								.priority("中")
								.taskKind("定期作業")
								.build(),
								new TaskSummaryItemView().builder()
								.groupId("1112")
								.title("仕事")
								.startTime("9:00:00")
								.endTime("17:00:00")
								.priority("高")
								.taskKind("仕事")
								.build(),
								new TaskSummaryItemView().builder()
								.groupId("1113")
								.title("夜活動")
								.startTime("17:00:00")
								.endTime("22:00:00")
								.priority("中")
								.taskKind("定期作業")
								.build(),
								new TaskSummaryItemView().builder()
								.groupId("1114")
								.title("N活動")
								.startTime("22:00:00")
								.endTime("23:00:00")
								.priority("中")
								.taskKind("定期作業")
								.build()
								)),
				Map.entry(
						"10/12",
						List.of(
								new TaskSummaryItemView().builder()
								.groupId("1121")
								.title("朝活動")
								.startTime("7:00:00")
								.endTime("9:00:00")
								.priority("中")
								.taskKind("定期作業")
								.build(),
								new TaskSummaryItemView().builder()
								.groupId("1122")
								.title("仕事")
								.startTime("9:00:00")
								.endTime("17:00:00")
								.priority("高")
								.taskKind("仕事")
								.build()
								)),
				Map.entry(
						"10/13",List.of(new TaskSummaryItemView())
						)
				
				);
		model.addAttribute("taskSummaryMaps",new TreeMap<>(TaskSummaryItemViewMaps));
		model.addAttribute("taskGroupCreateForm", new TaskGroupCreateForm());
		return "task/list";
	}
	
	@PostMapping("task/new")
	public String PostCreateTaskGroup(
			@ModelAttribute("taskGroupCreateForm") TaskGroupCreateForm form) {
		
		System.out.println(form.toString());
		
		return "redirect:/mypage/task/list";
	}
	

}
