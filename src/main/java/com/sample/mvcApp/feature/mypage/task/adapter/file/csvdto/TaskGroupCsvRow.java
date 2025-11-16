package com.sample.mvcApp.feature.mypage.task.adapter.file.csvdto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.sample.mvcApp.common.file.annotation.FileColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class TaskGroupCsvRow {

    @FileColumn(index = 0, header = "対象日", required = true)
    private LocalDate workYmd;

    @FileColumn(index = 1, header = "タイトル", required = true)
    private String title;

    @FileColumn(index = 2, header = "説明", required = false)
    private String description;

    @FileColumn(index = 3, header = "タスク種別", required = false)
    private String taskTypeCode;

    @FileColumn(index = 4, header = "優先度", required = true)
    private String priority;

    @FileColumn(index = 5, header = "予定開始", required = false)
    private LocalTime plannedStartTime;

    @FileColumn(index = 6, header = "予定終了", required = false)
    private LocalTime plannedEndTime;
 
}
