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
public class TaskDetailItemView {

        /** タスクグループID */
        private String groupId;

        /** 実施日 */
        private String workDate;

        /** タイトル */
        private String title;

        /** 説明 */
        private String description;

        /** タスクの種類 */
        private String taskKind;

        /** 優先度 */
        private String priority;

        /** 予定時間 */
        private String plannedTime;

        /** 実績時間 */
        private String actualTime;

        /** ステータス */
        private String status;

        /** テンプレートフラグ */
        private boolean template;
}