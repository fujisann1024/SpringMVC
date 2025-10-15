package com.sample.mvcApp.feature.mypage.task.adapter.db.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupInDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupOutDto;

public interface TaskGroupJpaRepository extends JpaRepository<TaskGroupOutDto, TaskGroupInDto> {
	
}
