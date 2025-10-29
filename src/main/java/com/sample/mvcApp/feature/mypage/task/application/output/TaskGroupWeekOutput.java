package com.sample.mvcApp.feature.mypage.task.application.output;

import java.time.LocalDate;
import java.util.Map;

public record TaskGroupWeekOutput(
		Map<LocalDate,TaskGroupSummaryOutput>  taskGroupByWeekMap
		) { }