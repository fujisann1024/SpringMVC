package com.sample.mvcApp.feature.mypage.task.domain.port;

import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.TaskGroupCollectionMap;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.WeekRange;

public interface TaskGroupQuery {

	/** 
	 * 指定された週範囲のタスクグループ一覧を取得する
	 * 
	 * @param weekRange 週範囲
	 * @return タスクグループ一覧
	 * 
	 *  */
	TaskGroupCollectionMap getTaskGroupWeekRange(WeekRange weekRange);
	
	/** 
	 * 指定されたタスクグループIDのタスクグループを取得する
	 * 
	 * @param taskGroupId タスクグループID
	 * @return タスクグループ
	 * 
	 *  */
	TaskGroup getTaskGroupById(TaskGroupId taskGroupId);
}
