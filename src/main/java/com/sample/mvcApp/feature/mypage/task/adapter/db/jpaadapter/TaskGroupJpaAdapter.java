package com.sample.mvcApp.feature.mypage.task.adapter.db.jpaadapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.mvcApp.common.util.DateUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.db.helper.TaskGroupDBHelper;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpa.TaskGroupJpaRepository;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupGateway;

@Repository
public class TaskGroupJpaAdapter implements TaskGroupGateway {
	
	@Autowired
	private TaskGroupJpaRepository taskGroupJpaRepository;

	@Override
	public void save(TaskGroup taskGroup) {
		
		TaskGroupDto dto = TaskGroupDBHelper.parseToTaskGroupDto(taskGroup);
		this.setSystemDataByCreate(dto);
		taskGroupJpaRepository.save(dto);
		
	}
	
	@Override
	public int saveAll(List<TaskGroup> taskGroupList) {
		List<TaskGroupDto> taskGroupDtoList = taskGroupList.stream()
				.map(entity -> TaskGroupDBHelper.parseToTaskGroupDto(entity))
				.peek(entity -> setSystemDataByCreate(entity))
				.toList();
		
		return taskGroupJpaRepository.saveAll(taskGroupDtoList).size();
	}
	
	/*
	 * 作成時の共通項目の登録内容を設定
	 */
	private void setSystemDataByCreate(TaskGroupDto dto) {
		var now = DateUtil.getNowOffsetDateTime();
		var creater = "system";
		dto.setCreatedAt(now);
		dto.setUpdatedAt(now);
		dto.setCreatedBy(creater);
		dto.setUpdatedBy(creater);
	}

}
