package com.sample.mvcApp.feature.mypage.task.adapter.web.form;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class TaskGroupCreateForm {
	
	/*タイトル**/
	private String title;
	
	/**
	 * 実施年月日
	 */
	private LocalDate targetYmd;
	
	/**
	 * 説明
	 */
	private String explanation;
	
	/**
	 * タスク種別
	 */
	private String taskKind;
	
	/**
	 * 優先度
	 * */
	private String priority;
	
	/**
	 * 開始予定時分秒
	 */
	private LocalTime plannedStart;
	
	/**
	 * 終了予定時分秒
	 */
	private LocalTime plannedEnd;
	
	
}
