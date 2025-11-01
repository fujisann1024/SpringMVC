package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.sample.mvcApp.common.exception.DomainObjectException;

public class TitleTest {

	 /* ===== 正常系 ===== */

    @ParameterizedTest(name = "[{index}] OK: \"{0}\"")
    @ValueSource(strings = { "A", "タスク名", "タイトル_001", "  前後空白を含む  " })
    @DisplayName("正常: 有効な文字列なら生成できる")
    void create_ok(String input) {
        assertDoesNotThrow(() -> new Title(input));
        Title t = new Title(input);
        assertEquals(input, t.value());
    }

    @Test
    @DisplayName("正常: ちょうど100文字はOK")
    void ok_when_length_100() {
        String s = "あ".repeat(100); // マルチバイトでも String.length() は100
        assertDoesNotThrow(() -> new Title(s));
        assertEquals(100, new Title(s).value().length());
    }

    /* ===== 異常系 ===== */

    @ParameterizedTest(name = "[{index}] NG(blank): \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\t", "\n", "　" }) // 半角/全角空白
    @DisplayName("異常: null/blankは例外")
    void ng_when_blank(String input) {
        DomainObjectException ex =
            assertThrows(DomainObjectException.class, () -> new Title(input));
        assertTrue(ex.getMessage().contains("タイトル")); // メッセージも軽く確認
    }

    @Test
    @DisplayName("異常: 101文字以上は例外")
    void ng_when_length_over_100() {
        String s = "あ".repeat(101);
        DomainObjectException ex =
            assertThrows(DomainObjectException.class, () -> new Title(s));
        assertTrue(ex.getMessage().contains("100文字以内"));
    }

}
