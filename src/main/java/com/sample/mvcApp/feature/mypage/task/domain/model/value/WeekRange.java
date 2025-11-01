package com.sample.mvcApp.feature.mypage.task.domain.model.value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sample.mvcApp.common.exception.DomainObjectException;

/**
 * 週範囲
 */
public final class WeekRange {
	private final LocalDate start;
	private final LocalDate end;

	private WeekRange(LocalDate start, LocalDate end) {
		if (start == null || end == null)
			throw new DomainObjectException("week range is required");
		if (!end.equals(start.plusDays(6)))
			throw new DomainObjectException("週は7日である必要があります");
		this.start = start;
		this.end = end;
	}

	/** 指定された開始日から週範囲を生成するファクトリーメソッド */
	public static WeekRange of(LocalDate start) {
		return new WeekRange(start, start.plusDays(6));
	}

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
	}

	/**
	 * 開始から終了までの日付の一覧を取得
	 * @return
	 */
	public List<LocalDate> getRangeDateList() {
		var dateList = new ArrayList<LocalDate>();
		for (LocalDate startDate = this.start; 
				!startDate.isAfter(this.end); 
			 startDate = startDate.plusDays(1)) 
		{
			dateList.add(startDate);
		}
		return dateList;

	}

}
