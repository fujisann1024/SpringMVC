package com.sample.mvcApp.feature.mypage.task.application.usecase;

import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupUploadInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupResultOutput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupWeekOutput;

/**
 * タスクグループユースケース
 */
public interface TaskGroupUseCase {
	
	/**
	 * タスクグループ作成
	 * 
	 * @param input タスクグループ作成入力
	 */
	public void create(TaskGroupCreateInput input);
	
	/**
	 * タスクグループ取得(週別)
	 */
	public TaskGroupWeekOutput getTaskGroupWeekRange();
	
	/**
	 * タスクグループアップロード
	 */
	public TaskGroupResultOutput uploadTaskGroup(TaskGroupUploadInput input);
	

}
