package com.sample.mvcApp.feature.mypage.task.application.usecase;

import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;

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
	

}
