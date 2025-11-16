package com.sample.mvcApp.feature.mypage.task.domain.port;

import java.util.List;

import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;

public interface TaskGroupGateway {

	public void save(TaskGroup taskGroup);
	
	public void saveAll(List<TaskGroup> taskGroupList);
}
