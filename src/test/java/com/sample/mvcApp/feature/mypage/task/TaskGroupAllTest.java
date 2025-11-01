package com.sample.mvcApp.feature.mypage.task;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.sample.mvcApp.feature.mypage.task.adapter.db.jdbcdao.TaskGroupJdbcDaoTest;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpa.TaskGroupJpaRepositoryTest;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpaadapter.TaskGroupJpaAdapterTest;
import com.sample.mvcApp.feature.mypage.task.adapter.web.controller.TaskGroupControllerTest;
import com.sample.mvcApp.feature.mypage.task.application.service.TaskGroupServiceTest;

@Suite
@SelectClasses({
  TaskGroupControllerTest.class,
  TaskGroupServiceTest.class,
  TaskGroupJdbcDaoTest.class,
  TaskGroupJpaRepositoryTest.class,
  TaskGroupJpaAdapterTest.class,
})
public class TaskGroupAllTest {

}
