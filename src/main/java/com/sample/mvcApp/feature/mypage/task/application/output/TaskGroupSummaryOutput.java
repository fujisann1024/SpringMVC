package com.sample.mvcApp.feature.mypage.task.application.output;

/**
 * タスクグループ概要出力DTO
 */
public record TaskGroupSummaryOutput(
		/** タスクグループID */
		String groupId,
		/** タイトル */
		String title,
		/** 開始予定時分秒 */
		String plannedStartTime,
		/** 終了予定時分秒 */
		String plannedEndTime,
		/** 優先度 */
		String priority,
		/** タスク種別 */
		String taskTypeCode
		) { }

