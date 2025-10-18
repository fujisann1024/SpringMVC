package com.sample.mvcApp.feature.mypage.task.application.input;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * タスクグループ作成入力
 * 
 * @param taskGroupId       タスクグループID
 * @param workYmd          実施年月日
 * @param title             タイトル
 * @param description       説明
 * @param taskTypeCode      タスク種別コード
 * @param priority          優先度
 * @param plannedStartTime  予定開始時間
 * @param plannedEndTime    予定終了時間
 */
public record TaskGroupCreateInput(
		String taskGroupId,
		LocalDate workYmd,
		String title,
		String description,
		String taskTypeCode,
		String priority,
		LocalTime plannedStartTime,
		LocalTime plannedEndTime
		) { }
