package com.sample.mvcApp.common.file.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.sample.mvcApp.common.file.annotation.FileColumn;
import com.sample.mvcApp.common.file.exception.FileImportException;
import com.sample.mvcApp.common.file.util.collection.CsvMappedRowCollection;
import com.sample.mvcApp.common.file.util.value.CsvMappedRow;
import com.sample.mvcApp.common.util.DateUtil;

public class CSVUtil {
	
    /**
     * 既定設定（先頭行＝ヘッダー行をスキップ）で CSV を読み、type のインスタンスへ行ごとにマッピング
     * @param inputStream CSV入力
     * @param type        マッピング対象クラス（デフォルトコンストラクタ必須）
     * @param hasHeader   先頭行がヘッダーなら true（スキップ）
     */
    public static <T> CsvMappedRowCollection<T> map(InputStream inputStream, Class<T> type, boolean hasHeader) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setTrim(true)
            .setIgnoreSurroundingSpaces(true)
            .build();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser parser = format.parse(reader)) {

            Map<Integer, FieldMeta> fieldMetaMap = extractFieldMeta(type);
            List<CsvMappedRow<T>> mappedRows = new ArrayList<>();

            //1行ごとに実行
            boolean headerSkipped = !hasHeader; // hasHeader=trueなら最初にスキップする
            for (CSVRecord record : parser) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                int csvRowNumber = (int) record.getRecordNumber();
                
                //読み込んだ内容からオブジェクトにマッピングし、結果を設定
                RowReadResult<T> rowResult = mapRow(record, csvRowNumber, type, fieldMetaMap);
                if (rowResult == null || rowResult.isEmptyRow()) continue;
                mappedRows.add(new CsvMappedRow<>(csvRowNumber, rowResult.payload(), rowResult.errors()));
            }
            return CsvMappedRowCollection.of(mappedRows);

        } catch (IOException ex) {
            throw new FileImportException("CSVファイルの読み込みに失敗しました。", ex);
        } catch (ReflectiveOperationException ex) {
            throw new FileImportException("CSVのマッピング処理に失敗しました。", ex);
        }
    }

    /**
     * 行変換
     * @param <T>
     * @param record 読み込んだCSV情報
     * @param csvRowNumber 行番号
     * @param type マッピング対象クラス
     * @param fieldMetaMap フィールドのメタ情報map
     * @return
     * @throws ReflectiveOperationException
     */
    private static <T> RowReadResult<T> mapRow(
            CSVRecord record,
            int csvRowNumber,
            Class<T> type,
            Map<Integer, FieldMeta> fieldMetaMap
        ) throws ReflectiveOperationException {

            T instance = type.getDeclaredConstructor().newInstance();
            List<String> errors = new ArrayList<>();
            boolean allBlank = true;

            for (Map.Entry<Integer, FieldMeta> entry : fieldMetaMap.entrySet()) {
                int columnIndex = entry.getKey();
                FieldMeta meta = entry.getValue();
                String cellValue = columnIndex < record.size() ? safeTrim(record.get(columnIndex)) : "";

                if (!cellValue.isEmpty()) {
                    allBlank = false;
                }

                //値が設定されていないかつ必須項目の場合、エラーメッセージを設定
                if (cellValue.isEmpty() && meta.required()) {
                     errors.add(formatMessage(csvRowNumber, meta.header(), "は必須です。"));
                    continue;
                }

                // 行変換
                try {
                    Object converted = convertValue(cellValue, meta.field().getType());
                    meta.field().set(instance, converted);
                } catch (ReflectiveOperationException | RuntimeException ex) {
                    errors.add(formatMessage(
                        csvRowNumber,
                        meta.header(),
                        String.format("の値[%s]を型[%s]に設定できません。%s",
                            cellValue, meta.field().getType().getSimpleName(), ex.getMessage())));
                }
            }

            // 読み込んだレコードがすべて空文字スペースなら
            if (allBlank) {
                return RowReadResult.empty();
            }
            return new RowReadResult<>(instance, errors, false);
        }
    
    private static String safeTrim(String s) {
        if (s == null) return "";
        // 全角スペースも除去したい場合は以下を有効化:
        // return s.replace("\u3000", " ").trim();
        return s.trim();
    }

    private static String formatMessage(int rowNumber, String header, String message) {
        String column = (header == null || header.isBlank()) ? "列" : header;
        return String.format("%d行目: %s%s", rowNumber, column, message);
    }


    /**
     * 継承も含めて @FileColumn のついたフィールドのメタ情報を抽出。
     * index の重複は IllegalStateException。
     * index 昇順で LinkedHashMap に格納。
     */
    private static Map<Integer, FieldMeta> extractFieldMeta(Class<?> type) {
        return allFields(type)
            .filter(f -> f.isAnnotationPresent(FileColumn.class))
            .map(FieldMeta::of)
            .sorted(Comparator.comparingInt(FieldMeta::index))
            .collect(Collectors.toMap(
                FieldMeta::index,
                m -> m,
                (a, b) -> { throw new FileImportException("indexが重複して設定されています" + a.index()); },
                LinkedHashMap::new
            ));
    }
    
    /**
     * マッピング対象クラスからフィールドの情報の一覧として取得する
     * @param type マッピング対象クラス
     * @return
     */
    private static Stream<Field> allFields(Class<?> type) {
        Stream.Builder<Field> b = Stream.builder();
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            Arrays.stream(c.getDeclaredFields()).forEach(b::add);
        }
        return b.build();
    }

    /**
     * ファイルから読み込んだ値をフィールドの型に合わせる
     * @param raw 読み込んだ値
     * @param targetType マッピング先のデータ型
     * @return
     */
    private static Object convertValue(String raw, Class<?> targetType) {
        String cellValue = raw;

        if (String.class.equals(targetType)) {
            return cellValue;
        }
        if (Boolean.class.equals(targetType) || boolean.class.equals(targetType)) {
            String v = cellValue.trim().toLowerCase();
            return v.equals("true") || v.equals("TRUE");
        }
        if (BigDecimal.class.equals(targetType)) {
            return new BigDecimal(cellValue);
        }
        if (LocalDate.class.equals(targetType)) {
            return DateUtil.toLocalDate(cellValue);
        }
        if (LocalTime.class.equals(targetType)) {
            return DateUtil.toLocalTime(cellValue);
        }
        if (LocalDateTime.class.equals(targetType)) {
            return DateUtil.toLocalDateTime(cellValue);
        }
        throw new FileImportException("未対応の型です: " + targetType.getName());
    }
    
    /**
     * フィールドメタ情報を保持するクラス
     */
    private record FieldMeta(
    	  /** 対象ドメインのリフレクション用 Field */
    	  Field field
    	  /** 入力ファイル上の列インデックス*/
    	, int index
    	/** 入力ファイルのヘッダ名（空ならフィールド名を使う）*/
    	, String header
    	/** 必須列かどうか*/
    	, boolean required
    	) {

        private FieldMeta {
            field.setAccessible(true);
        }

        static FieldMeta of(Field field) {
            FileColumn column = field.getAnnotation(FileColumn.class);
            return new FieldMeta(
            		  field
            		, column.index()
            		, resolveHeader(field, column.header())
            		, column.required());
        }

        /**
         * ヘッダー情報を取得(設定されていない場合はフィールド名を取得)
         * @param field
         * @param header
         * @return
         */
        static String resolveHeader(Field field, String header) {
            if (header != null && !header.isBlank()) {
                return header;
            }
            return field.getName();
        }
    }

    /**
     * 行あたり読込結果
     * 
     * <pre>
     * 1行あたりに読み込んだ結果をもつクラス
     * </pre>
     * 
     * @param <T> ファイルDtoの型
     */
    private static class RowReadResult<T> {
        private final T payload;
        private final List<String> errors;
        private final boolean emptyRow;

        RowReadResult(T payload, List<String> errors, boolean emptyRow) {
            this.payload = payload;
            this.errors = errors;
            this.emptyRow = emptyRow;
        }

        static <T> RowReadResult<T> empty() {
            return new RowReadResult<T>(null, List.of(), true);
        }

        /**
         * 空行かどうか
         * @return
         */
        boolean isEmptyRow() {
            return emptyRow;
        }

        /**
         * ファイルDtoインスタンス
         * @return
         */
        T payload() {
            return payload;
        }

        /**
         * 読み込んだ行に対するエラーの一覧
         * @return
         */
        List<String> errors() {
            return errors;
        }
    }
}
