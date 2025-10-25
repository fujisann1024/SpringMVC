package com.sample.mvcApp.feature.mypage.task.domain.model.aggregate;

import java.util.Optional;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;

/**
 * タスクグループ（集約根）
 *
 * 不変の値オブジェクト的に扱うため record を採用。
 * null 許可ポリシーは従来通り：id/title/priority は必須。
 */
public record TaskGroup(
        TaskGroupId id,
        Title title,
        String description,
        String taskTypeCode,
        Priority priority,
        TimeSlot plannedTime,
        TimeSlot actualTime,
        TaskStatus status,
        boolean template
) {

    /**
     * 正規化コンストラクタ（バリデーション & デフォルト補正）
     */
    public TaskGroup {
        // 必須チェック
        Optional.ofNullable(id).orElseThrow(() -> new DomainObjectException("TaskGroupId is required"));
        Optional.ofNullable(title).orElseThrow(() -> new DomainObjectException("Title is required"));
        Optional.ofNullable(priority).orElseThrow(() -> new DomainObjectException("Priority is required"));
    }

    /**
     * タスクグループ取得
     * ファクトリーメソッド
     *
     * @param id            タスクグループID
     * @param title         タイトル
     * @param description   説明
     * @param taskTypeCode  タスク種別コード
     * @param priority      優先度
     * @param planned       予定時間
     * @return              タスクグループ集約根
     */
    public static TaskGroup newCreate(
            TaskGroupId id,
            Title title,
            String description,
            String taskTypeCode,
            Priority priority,
            TimeSlot planned
    ) {
        return new TaskGroup(
                id,
                title,
                description,
                taskTypeCode,
                priority,
                planned,
                null,                    // actualTime
                TaskStatus.PLANNED,      // status
                false                    // template
        );
    }
}
