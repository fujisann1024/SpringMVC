package com.sample.mvcApp.feature.mypage.task.adapter.web.itemView;

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
public class TaskSummaryDetailItemView {

	/**
	 * タスクグループID
	 * */
	private String groupId;
	
	/*
	 * タイトル
	 */
	private String title;
	
	/**
	 * 開始予定時分秒
	 */
	private String startTime;
	
	/**
	 * 終了予定時分秒
	 */
	private String endTime;
	
	/**
	 * 優先度
	 * */
	private String priority;
	
	/**
	 * タスク種別
	 * */
	private String taskKind;
	

}
