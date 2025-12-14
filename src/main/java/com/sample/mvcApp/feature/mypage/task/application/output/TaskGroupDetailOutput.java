package com.sample.mvcApp.feature.mypage.task.application.output;

import java.time.LocalDate;

/**
 * タスクグループ詳細出力DTO
 */
public record TaskGroupDetailOutput(
        /** タスクグループID */
        String groupId,
        /** 実施日 */
        LocalDate workYmd,
        /** タイトル */
        String title,
        /** 説明 */
        String description,
        /** タスク種別 */
        String taskTypeCode,
        /** 優先度 */
        String priority,
        /** 予定開始時分秒 */
        String plannedStartTime,
        /** 予定終了時分秒 */
        String plannedEndTime,
        /** 当日開始時分秒 */
        String actualStartTime,
        /** 当日終了時分秒 */
        String actualEndTime,
        /** ステータス */
        String status,
        /** テンプレートフラグ */
        boolean template
        ) { }