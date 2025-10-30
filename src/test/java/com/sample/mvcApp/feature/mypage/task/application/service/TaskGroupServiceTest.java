package com.sample.mvcApp.feature.mypage.task.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.application.input.TaskGroupCreateInput;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.Priority;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskStatus;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupGateway;

@ExtendWith(MockitoExtension.class)
class TaskGroupServiceTest {

    @Mock
    TaskGroupGateway gateway;

    // フィールドインジェクションでも @InjectMocks がモックを差し込みます（Spring不要）
    @InjectMocks
    TaskGroupService service;
    
    ArgumentCaptor<TaskGroup> captor1 = ArgumentCaptor.forClass(TaskGroup.class);


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
