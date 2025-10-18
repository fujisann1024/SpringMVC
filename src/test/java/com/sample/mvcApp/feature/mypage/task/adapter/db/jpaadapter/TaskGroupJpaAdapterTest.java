package com.sample.mvcApp.feature.mypage.task.adapter.db.jpaadapter;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.sample.mvcApp.common.util.DateUtil;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupKeyDto;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;

@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DBUnit(
		caseInsensitiveStrategy = Orthography.LOWERCASE, 
		qualifiedTableNames = true
		) 
@Import(TaskGroupJpaAdapter.class)
class TaskGroupJpaAdapterTest {
	
	@Autowired
    TaskGroupJpaAdapter adapter;

    private final String testPath = "com/sample/mvcApp/feature/mypage/task/adapter/db/jpaadapter/";
	
	@Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // ← これを付ける
    @DisplayName("saveでINSERTでき、期待状態と一致する")
    @DataSet(value = testPath + "Test1_1.xlsx", cleanBefore = true) // 空テーブルで開始
    @ExpectedDataSet(value = testPath + "Test2_1.xlsx", orderBy = {"task_group.task_group_id", "task_group.work_ymd"})
    void save_insert_one_row() {
		
		 // Arrange
        TaskGroup aggregate = Mockito.mock(TaskGroup.class);
        MockedStatic<DateUtil> util = Mockito.mockStatic(DateUtil.class);

        TaskGroupDto dto = TaskGroupDto.builder()
        		.id(TaskGroupKeyDto.builder()
						.taskGroupId("TG003")
						.workYmd(LocalDate.parse("2025-10-15"))
						.build())
				.title("実装タスクB")
				.description("ユーザ更新API")
				.taskTypeCode("MEETING")
				.priority("低")
				.plannedStartTime(LocalTime.parse("18:00:00"))
				.plannedEndTime(LocalTime.parse("22:00:00"))
				.actualStartTime(LocalTime.parse("18:30:00"))
				.actualEndTime(LocalTime.parse("22:30:00"))
				.status("IN_PROGRESS")
				.isTemplate(true)
				.build();

        

        when(aggregate.toDto()).thenReturn(dto);
        util.when(DateUtil::getNowOffsetDateTime)
        .thenReturn(java.time.OffsetDateTime.parse("2025-10-14T08:40:00+09:00"));
      

        // Act
        adapter.save(aggregate);

        verify(aggregate, times(1)).toDto();
        
        // @ExpectedDataSet がテーブル内容＝ after_insert.xlsx と一致するか検証
    }

}
