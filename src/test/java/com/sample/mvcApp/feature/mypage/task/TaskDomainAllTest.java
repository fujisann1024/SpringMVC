package com.sample.mvcApp.feature.mypage.task;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroupTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.DayTaskGroupCollectionTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.TaskGroupCollectionMapTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.PriorityTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupIdTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatusTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlotTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TitleTest;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.WeekRangeTest;

@Suite
@SelectClasses({
	  TaskGroupTest.class,
	  PriorityTest.class,
	  TaskGroupIdTest.class,
	  TaskStatusTest.class,
	  TimeSlotTest.class,
	  TitleTest.class,
	  WeekRangeTest.class,
	  DayTaskGroupCollectionTest.class,
	  TaskGroupCollectionMapTest.class
	})
public class TaskDomainAllTest {

}
