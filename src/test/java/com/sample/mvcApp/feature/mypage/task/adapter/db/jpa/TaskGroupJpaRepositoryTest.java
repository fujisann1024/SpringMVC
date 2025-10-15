package com.sample.mvcApp.feature.mypage.task.adapter.db.jpa;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupInDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupOutDto;

@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DBUnit(
		caseInsensitiveStrategy = Orthography.LOWERCASE, 
		qualifiedTableNames = true
		) // or CaseInsensitiveStrategy.LOWERCASE
class TaskGroupJpaRepositoryTest {

    @Autowired
    TaskGroupJpaRepository repo;
    
    private final String testPath = "com/sample/mvcApp/feature/mypage/task/adapter/db/jpa/";

    @Test
    @DisplayName("findByIdで1件取得できる")
    @DataSet(value = testPath + "Test1_1.xlsx", cleanBefore = true)
    void findById_fromExcel() {
        
        var id = TaskGroupInDto.builder()
        		.taskGroupId("TG001")
        		.workYmd(LocalDate.parse("2025-10-14"))
        		.build();

        Optional<TaskGroupOutDto> found = repo.findById(id);

        assertThat(found).isPresent()
        .hasValueSatisfying(e -> {
            assertThat(e.getTitle()).isEqualTo("朝活");
            assertThat(e.getDescription()).isEqualTo("読書と軽い運動");
            assertThat(e.getTaskTypeCode()).isEqualTo("PERSONAL");
            assertThat(e.getPriority()).isEqualTo("高");
            assertThat(e.getPlannedStartTime()).isEqualTo(LocalTime.parse("06:30:00"));
            assertThat(e.getPlannedEndTime()).isEqualTo(LocalTime.parse("07:30:00"));
            assertThat(e.getActualStartTime()).isEqualTo(LocalTime.parse("06:35:00"));
            assertThat(e.getActualEndTime()).isEqualTo(LocalTime.parse("07:25:00"));
            assertThat(e.getStatus()).isEqualTo("DONE");
            assertThat(e.isTemplate()).isEqualTo(false);
        });

    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // ← これを付ける
    @DisplayName("saveでINSERTでき、期待状態と一致する")
    @DataSet(value = testPath + "Test1_1.xlsx", cleanBefore = true) // 空テーブルで開始
    @ExpectedDataSet(value = testPath + "Test2_1.xlsx", orderBy = {"task_group.task_group_id", "task_group.work_ymd"})
    void save_insert_one_row() {
        var e = TaskGroupOutDto.builder()
				.id(TaskGroupInDto.builder()
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
				.createdAt(OffsetDateTime.parse("2025-10-14T08:40:00+09:00"))
				.createdBy("ccc")
				.updatedAt(OffsetDateTime.parse("2025-10-14T11:55:00+09:00"))
				.updatedBy("ccc")
				.build();
        repo.saveAndFlush( e);
        // @ExpectedDataSet がテーブル内容＝ after_insert.xlsx と一致するか検証
    }

    @Test
    @DisplayName("deleteByIdで削除できる")
    @DataSet(value =  testPath + "Test1_1.xlsx", cleanBefore = true)
    void delete_by_id() {
        TaskGroupInDto id = new TaskGroupInDto("TG002", LocalDate.parse("2025-10-14"));

        assertThat(repo.existsById(id)).isTrue();
        repo.deleteById(id);
        assertThat(repo.existsById(id)).isFalse();
    }

}
