package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.sample.mvcApp.common.exception.DomainObjectException;

class PriorityTest {

	@ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
	@DisplayName("parsePriority: 正しいラベルは対応する列挙へ変換される")
	@CsvSource({
			"高, HIGH",
			"中, MEDIUM",
			"低, LOW"
	})
	void parsePriority_mapsCorrectly(String label, Priority expected) {
		assertEquals(expected, Priority.parsePriority(label));
	}

	@Test
	@DisplayName("parsePriority: null は DomainObjectException")
	void parsePriority_null_throws() {
		assertThrows(DomainObjectException.class, () -> Priority.parsePriority(null));
	}

	@Test
	@DisplayName("parsePriority: 未知ラベルは DomainObjectException")
	void parsePriority_unknown_throws() {
		assertThrows(DomainObjectException.class, () -> Priority.parsePriority("最優先"));
	}

}
