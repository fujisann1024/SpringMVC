package com.sample.mvcApp.feature.mypage.task.adapter.db.jdbcdao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.TaskGroupCollectionMap;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.WeekRange;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupQuery;

@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, qualifiedTableNames = true)
@Import(TaskGroupJdbcDao.class)
public class TaskGroupJdbcDaoTest {

	@Autowired
	TaskGroupQuery dao;

	private final String testPath = "com/sample/mvcApp/feature/mypage/task/adapter/db/jdbcdao/";

	@Test
	@DisplayName("getTaskGroupWeekRangeで想定の結果と一致すること")
	@DataSet(value = testPath + "Test1_1.xlsx", cleanBefore = true)
	void getTaskGroupWeekRange1() {
		var weekRange = WeekRange.of(LocalDate.of(2025, 10, 20));

		TaskGroupCollectionMap map = dao.getTaskGroupWeekRange(weekRange);

		// 範囲外が入っていないこと
		assertThat(map.getByDate().get(LocalDate.of(2025, 10, 19))).isNull();
		assertThat(map.getByDate().get(LocalDate.of(2025, 10, 27))).isNull();
		
		// 範囲内の件数
		assertEquals(11, map.asFlatList().size());
		
		
		Function<TaskGroup, String> gid = tg -> tg.id().groupId();
		
		assertThat(map.getByDate()
				       .get(LocalDate.of(2025,10,20))
				       .getTaskGroups()
				       .stream().map(gid))
        		.containsExactlyInAnyOrder("TG020A", "TG020B");
		
		assertThat(map.getByDate()
				       .get(LocalDate.of(2025,10,22))
				       .getTaskGroups()
				       .stream().map(gid))
				.containsExactlyInAnyOrder("TG022B", "TG022A", "TG022C");
		
		assertThat(map.getByDate()
				       .get(LocalDate.of(2025,10,24))
				       .getTaskGroups()
				       .stream().map(gid))
				.containsExactlyInAnyOrder("TG024A");
		
		assertThat(map.getByDate()
				       .get(LocalDate.of(2025,10,25))
				       .getTaskGroups()
				       .stream().map(gid))
				.containsExactlyInAnyOrder("TG025A", "TG025B", "TG025C");
		
		assertThat(map.getByDate()
				       .get(LocalDate.of(2025,10,26))
				       .getTaskGroups()
				       .stream().map(gid))
				.containsExactlyInAnyOrder("TG026A", "TG026B");
		
	}

}
