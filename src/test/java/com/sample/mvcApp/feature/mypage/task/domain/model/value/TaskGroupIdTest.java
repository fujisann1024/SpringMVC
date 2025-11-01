package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.sample.mvcApp.common.exception.DomainObjectException;

public class TaskGroupIdTest {

    @Test
    @DisplayName("正常：必須2項目で生成できる")
    void create_ok() {
        var id = new TaskGroupId("TG001", LocalDate.parse("2025-10-15"));
        assertEquals("TG001", id.groupId());
        assertEquals(LocalDate.parse("2025-10-15"), id.workYmd());
    }
    
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " ", "\t", "\n", "　" }) // 空・空白（半角/全角）すべてNG
    @DisplayName("異常：groupIdがnull/blankなら例外")
    void ng_when_groupId_null_or_blank(String groupId) {
        assertThrows(DomainObjectException.class,
            () -> new TaskGroupId(groupId, LocalDate.parse("2025-10-15")));
    }

    @Test
    @DisplayName("異常：workYmdがnullなら例外")
    void ng_when_workYmd_null() {
        assertThrows(DomainObjectException.class,
            () -> new TaskGroupId("TG001", null));
    }

}
