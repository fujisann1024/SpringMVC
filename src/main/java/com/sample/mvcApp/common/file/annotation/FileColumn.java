package com.sample.mvcApp.common.file.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Excelの列とフィールドのマッピングを表現するアノテーション。
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface FileColumn {
    /**
     * 0始まりの列インデックス。
     */
    int index();

    /**
     * エラー表示などで利用する列名。
     */
    String header() default "";

    /**
     * 必須列かどうか。
     */
    boolean required() default false;
}
