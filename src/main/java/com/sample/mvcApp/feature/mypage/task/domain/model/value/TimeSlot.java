package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import java.time.LocalTime;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.common.util.DateUtil;

/**
 * 時間帯
 * 
 * @param startTime 開始時間
 * @param endTime 終了時間
 */
public record TimeSlot(LocalTime startTime, LocalTime endTime) {

	/**
	 * コンストラクタ
	 * 
	 * @param startTime 開始時間
	 * @param endTime 終了時間
	 * @throws IllegalArgumentException 不正な時間帯の場合
	 */
	public TimeSlot {

		if (startTime == null && endTime == null) {
            // 未設定はOK
        } else if (startTime == null) {
            throw new DomainObjectException("開始時間は必須です");
        } else if (endTime == null) {
            throw new DomainObjectException("終了時間は必須です");
        } else {
            // 半開区間: 長さ > 0 を要求（end == start はNG）
            if (!endTime.isAfter(startTime)) {
                throw new DomainObjectException("終了時間は開始時間より後でなければなりません");
            }
        }

	}
	
	public String getStartTimeHHmm() {
		return DateUtil.formatLocalTime(startTime, "HH:mm");
	}
	
	public String getEndTimeHHmm() {
		return DateUtil.formatLocalTime(endTime, "HH:mm");
	}

}
