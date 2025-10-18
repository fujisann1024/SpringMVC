package com.sample.mvcApp.feature.mypage.task.adapter.db.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupKeyDto;

public interface TaskGroupJpaRepository extends JpaRepository<TaskGroupDto, TaskGroupKeyDto> {
	
}
