package com.sample.mvcApp.common.file.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sample.mvcApp.common.file.annotation.FileColumn;
import com.sample.mvcApp.common.file.exception.FileImportException;
import com.sample.mvcApp.common.file.util.collection.CsvMappedRowCollection;

public class CSVUtilTest {
	// --- テスト用の CSV DTO ---
	public static class TestCsvRow {

		@FileColumn(index = 0, header = "対象日", required = true)
		private LocalDate targetYmd;

		@FileColumn(index = 1, header = "タイトル", required = true)
		private String title;

		@FileColumn(index = 2, header = "説明", required = false)
		private String description;

		@FileColumn(index = 3, header = "開始", required = true)
		private LocalTime start;

		@FileColumn(index = 4, header = "終了", required = true)
		private LocalTime end;
		
		@FileColumn(index = 5, header = "番号", required = false)
		private BigDecimal number;
		
		@FileColumn(index = 6, header = "フラグ", required = false)
		private Boolean flag;
		
		@FileColumn(index = 7, header = "日時", required = false)
		private LocalDateTime dateTime;

		public LocalDate getTargetYmd() {
			return targetYmd;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public LocalTime getStart() {
			return start;
		}

		public LocalTime getEnd() {
			return end;
		}
		
		public BigDecimal getNumber() {
			return number;
		}
		public Boolean getFlag() {
			return flag;
		}
		public LocalDateTime getDateTime() {
			return dateTime;
		}
	}

	@Test
	@DisplayName("正常：CSV を DTO にマッピングできる（2行）")
	void map_success_2rows() {

		String csv = """
				対象日,タイトル,説明,開始,終了,番号,フラグ,日時
				2025-10-01,API製造,基盤APIを製造,10:00,12:00,1.5,true,2025-10-01T10:00:00
				2025-10-02,APIテスト,基盤APIをテスト,13:00,15:00,2.5,false,2025-10-02T13:00:00
				""";

		ByteArrayInputStream stream = new ByteArrayInputStream(csv.getBytes());

		CsvMappedRowCollection<TestCsvRow> result = CSVUtil.map(stream, TestCsvRow.class, true);

		assertFalse(result.isError());
		assertEquals(2, result.getCsvRowList().size());

		TestCsvRow r1 = result.getCsvRowList().get(0).payload();
		TestCsvRow r2 = result.getCsvRowList().get(1).payload();

		// row1
		assertEquals(LocalDate.of(2025, 10, 1), r1.getTargetYmd());
		assertEquals("API製造", r1.getTitle());
		assertEquals("基盤APIを製造", r1.getDescription());
		assertEquals(LocalTime.of(10, 0), r1.getStart());
		assertEquals(LocalTime.of(12, 0), r1.getEnd());
		assertEquals(new BigDecimal("1.5"), r1.getNumber());
		assertEquals(Boolean.TRUE, r1.getFlag());
		assertEquals(LocalDateTime.of(2025, 10, 1, 10, 0, 0), r1.getDateTime());

		// row2
		assertEquals(LocalDate.of(2025, 10, 2), r2.getTargetYmd());
		assertEquals("APIテスト", r2.getTitle());
		assertEquals("基盤APIをテスト", r2.getDescription());
		assertEquals(LocalTime.of(13, 0), r2.getStart());
		assertEquals(LocalTime.of(15, 0), r2.getEnd());
		assertEquals(new BigDecimal("2.5"), r2.getNumber());
		assertEquals(Boolean.FALSE, r2.getFlag());
		assertEquals(LocalDateTime.of(2025, 10, 2, 13, 0, 0), r2.getDateTime());
	}

	@Test
	@DisplayName("空行は RowReadResult.empty() でスキップ")
	void map_empty_row() {

		String csv = """
				対象日,タイトル,説明,開始,終了,番号,フラグ,日時

				2025-10-01,A,B,09:00,10:00,,,
				""";

		var stream = new ByteArrayInputStream(csv.getBytes());
		var result = CSVUtil.map(stream, TestCsvRow.class, true);

		assertEquals(1, result.getCsvRowList().size());
	}

	@Test
	@DisplayName("ヘッダー無しモード(hasHeader=false)で1行読み込み")
	void map_no_header() {

		String csv = "2025-10-01,A,B,09:00,10:00,1,true,2025-10-01T10:00:00";

		var stream = new ByteArrayInputStream(csv.getBytes());
		var result = CSVUtil.map(stream, TestCsvRow.class, false);

		assertEquals(1, result.getCsvRowList().size());
	}

	@Test
	@DisplayName("異常：必須項目が空の場合 errors を保持する")
	void map_error_required_field() {

		String csv = """
				対象日,タイトル,説明,開始,終了,番号,フラグ,日時
				,,説明文,10:00,12:00,1,true,2025-10-01T10:00:00
				""";

		ByteArrayInputStream stream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));

		CsvMappedRowCollection<TestCsvRow> result = CSVUtil.map(stream, TestCsvRow.class, true);

		assertTrue(result.isError());
		assertEquals(1, result.getCsvRowList().size());

		Map<Integer, List<String>> errorsMap = result.getAllErrorsMap(); // 1行目

		assertEquals(1, errorsMap.size());
		assertEquals(2, errorsMap.get(2).size());
		assertEquals(errorsMap.get(2).get(0), "2行目: 対象日は必須です。");
		assertEquals(errorsMap.get(2).get(1), "2行目: タイトルは必須です。");
	}

	@Test
	@DisplayName("異常：型変換に失敗すると errors が返る(LocalTime)")
	void map_error_type_convert() {

		String csv = """
				対象日,タイトル,説明,開始,終了,番号,フラグ,日時
				2025-10-01,API製造,説明文,INVALID,12:00,1,true,2025-10-01T10:00:00
				""";

		ByteArrayInputStream stream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));

		CsvMappedRowCollection<TestCsvRow> result = CSVUtil.map(stream, TestCsvRow.class, true);

		assertTrue(result.isError());

		Map<Integer, List<String>> errorsMap = result.getAllErrorsMap(); // 1行目
		assertEquals(1, errorsMap.size());
		assertEquals(1, errorsMap.get(2).size());
		assertEquals(errorsMap.get(2).get(0), "2行目: 開始の値[INVALID]を型[LocalTime]に設定できません。");
	}

	public static class UnsupportedTypeDto {
        @FileColumn(index = 0)
        private char val;  // 未対応型
    }

    @Test
    @DisplayName("未対応型は FileImportException")
    void map_unsupported_type() {

        String csv = """
                val
                abc
                """;

    	ByteArrayInputStream stream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));

		var result = CSVUtil.map(stream, UnsupportedTypeDto.class, true);

		assertTrue(result.isError());
    }
	
	@Test
	@DisplayName("IOException を捕捉して FileImportException に変換")
	void map_ioexception() {

		InputStream badStream = new InputStream() {
			@Override
			public int read() throws IOException {
				throw new IOException("forced");
			}
		};

		var exe = assertThrows(FileImportException.class,
				() -> CSVUtil.map(badStream, TestCsvRow.class, true));
		
		assertEquals("CSVファイルの読み込みに失敗しました。", exe.getMessage());
	}

	public static class BadDto {

		@FileColumn(index = 0)
		private String x;

		private BadDto() {
		} // newInstance できない
	}

	@Test
	@DisplayName("ReflectiveOperationException を捕捉")
	void map_reflective_exception() {

		String csv = """
				x
				abc
				""";

		var stream = new ByteArrayInputStream(csv.getBytes());

		var exe = assertThrows(FileImportException.class,
				() -> CSVUtil.map(stream, BadDto.class, true));
		
		assertEquals("CSVファイルの読み込みに失敗しました。", exe.getMessage());
	}

	public static class DuplicateColumnDto {
		@FileColumn(index = 0)
		private String a;

		@FileColumn(index = 0)
		private String b;
	}

	@Test
	@DisplayName("index 重複は FileImportException")
	void map_duplicate_index() {

		assertThrows(FileImportException.class,
				() -> CSVUtil.map(
						new ByteArrayInputStream("a,b".getBytes()),
						DuplicateColumnDto.class,
						true));
	}

	public static class TrimNullDto {
		@FileColumn(index = 1) // CSVに0列しか無い → safeTrim(null)
		private String x;
	}

	@Test
	@DisplayName("safeTrim(null) のパスを通す")
	void map_safeTrim_null() {

		String csv = "col0\nvalue";

		var result = CSVUtil.map(
				new ByteArrayInputStream(csv.getBytes()),
				TrimNullDto.class, true);

		assertFalse(result.isError());
	}

}
