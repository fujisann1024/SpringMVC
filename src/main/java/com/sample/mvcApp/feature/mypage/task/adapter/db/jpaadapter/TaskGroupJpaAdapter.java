package com.sample.mvcApp.feature.mypage.task.adapter.db.jpaadapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.mvcApp.common.util.DateUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.db.helper.TaskGroupDBHelper;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpa.TaskGroupJpaRepository;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupGateway;

@Repository
public class TaskGroupJpaAdapter implements TaskGroupGateway {
	
	@Autowired
	private TaskGroupJpaRepository taskGroupJpaRepository;

	@Override
	public void save(TaskGroup taskGroup) {
		
		var now = DateUtil.getNowOffsetDateTime();
		var creater = "system";
		var dto = TaskGroupDBHelper.parseToTaskGroupDto(taskGroup);
		dto.setCreatedAt(now);
		dto.setUpdatedAt(now);
		dto.setCreatedBy(creater);
		dto.setUpdatedBy(creater);
		taskGroupJpaRepository.save(dto);
		
	}

}
