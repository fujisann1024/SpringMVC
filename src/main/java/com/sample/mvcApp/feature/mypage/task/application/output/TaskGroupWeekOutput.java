package com.sample.mvcApp.feature.mypage.task.application.output;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record TaskGroupWeekOutput(
		LocalDate weekStartDate,
		LocalDate weekEndDate,
		Map<LocalDate, List<TaskGroupSummaryOutput>>  taskGroupByWeekMap
		) { }