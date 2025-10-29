package com.sample.mvcApp.feature.mypage.task.application.output;

public record TaskGroupWeekOutput(
		Map<LocalDate,TaskGroupSummaryOutput>  taskGroupByWeekMap,
		) { }