package com.sample.mvcApp.feature.mypage.task.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.common.props.SysdateProps;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupSummaryOutput;
import com.sample.mvcApp.feature.mypage.task.application.output.TaskGroupWeekOutput;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.TaskGroupCollectionMap;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Title;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.WeekRange;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupGateway;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupQuery;


@ExtendWith(MockitoExtension.class)
public class TaskGroupServiceTest {

    @Mock
    TaskGroupGateway gateway;
    
    @Mock 
    TaskGroupQuery query;
    
    @Mock 
    SysdateProps props;

    // フィールドインジェクションでも @InjectMocks がモックを差し込みます（Spring不要）
    @InjectMocks
    TaskGroupService service;
    
    ArgumentCaptor<TaskGroup> captor1 = ArgumentCaptor.forClass(TaskGroup.class);
    
    ArgumentCaptor<WeekRange> captor2 = ArgumentCaptor.forClass(WeekRange.class);
    
	private TaskGroupId id(String gid, String ymd) {
		return new TaskGroupId(gid, LocalDate.parse(ymd));
	}

	private TimeSlot slot(String start, String end) {
		return new TimeSlot(LocalTime.parse(start), LocalTime.parse(end));
	}



    @Test
    @DisplayName("正常：入力から集約を構築し repository.save を1回呼ぶ")
    void create_buildsAggregate_and_saves() {
    	
    	var input =  new TaskGroupCreateInput(
    			"TG001"
    			,LocalDate.parse("2025-10-15")
    			,"実装タスク"
    			,"API実装"
    			,"DEV"
    			,"高"
    			,LocalTime.parse("09:00:00")
    			,LocalTime.parse("12:00:00")
    			);
    	
    	 // Act
        service.create(input);

        // Assert
        verify(gateway, times(1)).save(captor1.capture());
        TaskGroup saved = captor1.getValue();

        assertAll("TaskGroup aggregate built from input",
            () -> assertEquals("TG001", saved.id().groupId()),
			() -> assertEquals(LocalDate.parse("2025-10-15"), saved.id().workYmd()),
			() -> assertEquals("実装タスク", saved.title().value()),
			() -> assertEquals("API実装", saved.description().orElse(null)),
			() -> assertEquals("DEV", saved.taskTypeCode().orElse(null)),
			() -> assertEquals(Priority.HIGH, saved.priority()),
			() -> assertEquals(LocalTime.parse("09:00:00"), saved.plannedTime().orElse(null).startTime()),
			() -> assertEquals(LocalTime.parse("12:00:00"), saved.plannedTime().orElse(null).endTime()),
			() -> assertNull(saved.actualTime().orElse(null)),
			() -> assertEquals(TaskStatus.PLANNED, saved.status()),
			() -> assertFalse(saved.template())
        );
    	
    }
    
    @Test
    @DisplayName("正常：週範囲の Map を DTO に整形して返す（予定あり・なし混在）")
    void getWeekRange_success() {
        // given: props による「今週月曜」を固定
        when(props.isUseflag()).thenReturn(true);
        when(props.getValue()).thenReturn(LocalDate.of(2025, 10, 15));
        
        var title1 = new Title("実装タスク1");
        var title2 = new Title("実装タスク2");
        var description  = "API実装";
        var taskTypeCode = "DEV";
        var priority = Priority.HIGH;
        
        
        List<TaskGroup> list = List.of(
        		TaskGroup.newCreate(
    					id("TG001", "2025-10-15"),
    					title1,
    					description,
    					taskTypeCode,
    					priority,
    					slot("09:00:00", "12:00:00")),
        		TaskGroup.newCreate(
    					id("TG002", "2025-10-15"),
    					title1,
    					description,
    					taskTypeCode,
    					priority,
    					slot("12:00:00", "15:00:00")),
        		TaskGroup.newCreate(
    					id("TG001", "2025-10-16"),
    					title2,
    					description,
    					taskTypeCode,
    					priority,
    					slot("09:00:00", "12:00:00")),
        		TaskGroup.newCreate(
    					id("TG002", "2025-10-16"),
    					title2,
    					description,
    					taskTypeCode,
    					priority,
    					slot("12:00:00", "15:00:00"))
        		);
        TaskGroupCollectionMap map = TaskGroupCollectionMap.of(list);
        when(query.getTaskGroupWeekRange(any(WeekRange.class))).thenReturn(map);

        // when
        TaskGroupWeekOutput out = service.getTaskGroupWeekRange();
        
        // then
        verify(query, times(1)).getTaskGroupWeekRange(captor2.capture());
        WeekRange range = captor2.getValue();
        assertEquals(range.getStart(), LocalDate.of(2025, 10, 15));
        assertEquals(range.getEnd(), LocalDate.of(2025, 10, 21));
        
        // 週 7 日ぶんのキーがある（存在しない日は空リスト）
        assertThat(out.taskGroupByWeekMap()).hasSize(7);

        List<TaskGroupSummaryOutput> outDetailList1 = out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 15));
        List<TaskGroupSummaryOutput> expectedlist1 = List.of(
        		new TaskGroupSummaryOutput("TG001","実装タスク1","09:00","12:00","高","DEV"),
        		new TaskGroupSummaryOutput("TG002","実装タスク1","12:00","15:00","高","DEV")
        		);
        for(int i = 0; i < outDetailList1.size(); i++) {
        	assertEquals(outDetailList1.get(i).toString(), expectedlist1.get(i).toString());
        }
        
        List<TaskGroupSummaryOutput> outDetailList2 = out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 16));
        List<TaskGroupSummaryOutput> expectedlist2 = List.of(
        		new TaskGroupSummaryOutput("TG001","実装タスク2","09:00","12:00","高","DEV"),
        		new TaskGroupSummaryOutput("TG002","実装タスク2","12:00","15:00","高","DEV")
        		);
        for(int i = 0; i < outDetailList2.size(); i++) {
        	assertEquals(outDetailList2.get(i).toString(), expectedlist2.get(i).toString());
        }
        assertAll("empty list",
        	() -> assertEquals(out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 17)).size(), 0),
        	() -> assertEquals(out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 18)).size(), 0),
        	() -> assertEquals(out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 19)).size(), 0),
        	() -> assertEquals(out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 20)).size(), 0),
        	() -> assertEquals(out.taskGroupByWeekMap().get(LocalDate.of(2025, 10, 21)).size(), 0)
        );
        
    }

    
    
    @Test
    @DisplayName("異常：不正な優先度で DomainObjectException が伝播する")
    void create_throws_on_invalid_priority() {
    	
    	var input =  new TaskGroupCreateInput(
    			"TG001"
    			,LocalDate.parse("2025-10-15")
    			,"実装タスク"
    			,"API実装"
    			,"DEV"
    			,"最優先" // 不正な優先度
    			,LocalTime.parse("09:00:00")
    			,LocalTime.parse("12:00:00")
    			);
    	

        // Act & Assert
        assertThrows(DomainObjectException.class, () -> service.create(input));
        // 失敗時は save が呼ばれていない
        verify(gateway, never()).save(any());
    }
}
