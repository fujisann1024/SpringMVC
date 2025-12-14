package com.sample.mvcApp.feature.mypage.task.application.input;

import java.time.LocalDate;

/**
 * タスクグループ詳細入力
 * 
 * @param groupId   グループID
 * @param workYmd   実施年月日
 */
public record TaskGroupDetailInput(
		  String groupId
		, LocalDate workYmd
		) {}
