package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.sample.mvcApp.common.exception.DomainObjectException;

/** TaskStatus列挙型のテスト */
class TaskStatusTest {

	@ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
	@DisplayName("fromLabel: 正しいラベルは対応する列挙へ変換される")
	@CsvSource({
			"未着手,   PLANNED",
			"進行中,   IN_PROGRESS",
			"完了,     DONE",
			"キャンセル, CANCELED",
			"保留,     ON_HOLD"
	})
	void fromLabel_mapsCorrectly(String label, TaskStatus expected) {
		assertEquals(expected, TaskStatus.fromLabel(label));
	}

	@Test
	@DisplayName("fromLabel: null は DomainObjectException")
	void fromLabel_null_throws() {
		assertThrows(DomainObjectException.class, () -> TaskStatus.fromLabel(null));
	}

	@Test
	@DisplayName("fromLabel: 未知ラベルは DomainObjectException")
	void fromLabel_unknown_throws() {
		assertThrows(DomainObjectException.class, () -> TaskStatus.fromLabel("未知"));
	}
}
