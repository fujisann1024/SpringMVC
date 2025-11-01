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

	private Map<LocalDate, List<TaskSummaryDetailItemView>> TaskSummaryItemViewMaps;
}
