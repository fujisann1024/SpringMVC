package com.sample.mvcApp.feature.mypage.task.application.usecase;

import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupDetailInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupDetailOutput;

public interface TaskGroupDetailUseCase {
	/**
     * タスクグループ詳細取得
     */
    public TaskGroupDetailOutput getTaskGroupDetail(TaskGroupDetailInput input);
}
