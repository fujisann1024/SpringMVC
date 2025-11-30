package com.sample.mvcApp.feature.mypage.task.adapter.web.itemView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class TaskSummaryItemView {
	
	/**
	 * 当週開始日
	 */
	private String currentWeekStart;
	
	/**
	 * 当週終了日
	 */
	private String currentWeekEnd;
	
	/**
	 * 前週開始日
	 */
	private String previousWeekStart;
	
	/**
	 * 次週開始日
	 */
	private String nextWeekStart;

	private Map<LocalDate, List<TaskSummaryDetailItemView>> TaskSummaryItemViewMaps;
}
