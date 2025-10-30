package com.sample.mvcApp.feature.mypage.task.domain.model.collection;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TimeSlot;

/**
 * 日付別タスクグループコレクション
 * 
 * <pre>
 * 特定の日付に紐づくタスクグループの集合
 * </pre>
 * 
 * @param taskDate   タスク日付
 * @param taskGroups タスクグループ一覧
 */
public final class DayTaskGroupCollection{

		/** タスク日付 */
		private final LocalDate taskDate;
		/** タスクグループ一覧 */
		private final List<TaskGroup> taskGroups;
	
	/**
	 * コンストラクタ
	 * @param taskDate
	 * @param taskGroups
	 * @throws DomainObjectException 不正な値オブジェクトの場合
	 */
	private DayTaskGroupCollection(LocalDate taskDate, List<TaskGroup> taskGroups) {

		this.taskDate = requireNonNull(taskDate,"taskDate is required");
		this.taskGroups = requireNonNull(taskGroups, "taskGroups is required");
		
	}

	/**
	 * ファクトリーメソッド
	 * 
	 * <pre>
	 * 指定された日付とタスクグループ一覧から日付別タスクグループコレクションを生成する。
	 * 同一IDのタスクグループが存在する場合や、タスクグループの予定時間が重複している場合は例外をスローする。
	 * </pre>
	 * 
	 * @param taskDate   タスク日付
	 * @param taskGroups タスクグループ一覧
	 * @return 日付別タスクグループコレクション
	 * @throws DomainObjectException 不正な値オブジェクトの場合
	 */
	public static DayTaskGroupCollection of(LocalDate taskDate, List<TaskGroup> taskGroups) {
		
		// 引数チェック
		requireNonNull(taskDate, "taskDate is required");
		requireNonNull(taskGroups, "taskGroups is required");
	
		// 日付の検証（id.workYmd が date のもののみ受け付ける）
		List<TaskGroup> filteredTaskGroupList = taskGroups.stream()
				.filter(g -> taskDate.equals(g.id().workYmd()))
				.collect(Collectors.toList());

		// 同一ID重複禁止
		var seen = new HashSet<TaskGroupId>();
		for (var g : filteredTaskGroupList) {
			if (!seen.add(g.id()))
				throw new DomainObjectException("同じグループワーク情報が存在します" + g.id());
		}

        // 開始時刻 → 優先度（高→中→低）→ ID の順で安定ソート
		Comparator<TaskGroup> byStartTime =
		        Comparator.comparing(
		            (TaskGroup g) -> g.plannedTime()
		                                     .map(TimeSlot::startTime)
		                                     .orElse(null),
		            Comparator.nullsLast(Comparator.naturalOrder()));
		
		Comparator<TaskGroup> byPriority =
		        Comparator.comparingInt(g -> g.priority().getSortNo()); // 小さいほど高優先
		
		Comparator<TaskGroup> byId =
						        Comparator.comparing(g -> g.id().groupId());
		
		
		filteredTaskGroupList.sort(byStartTime.thenComparing(byPriority).thenComparing(byId));

		// 今のタスクと次のタスクの時間帯が重複していないかチェック
		for (int i = 1; i < filteredTaskGroupList.size(); i++) {
			LocalTime prevEndTime = filteredTaskGroupList.get(i - 1)
												 .plannedTime()
												 .map(TimeSlot::endTime)
											     .orElse(null); 
			LocalTime currStartTime = filteredTaskGroupList.get(i)
					                             .plannedTime()
					                             .map(TimeSlot::startTime)
					                             .orElse(null);
			
			// どちらかが null の場合はスキップ
			if (prevEndTime == null || currStartTime == null) {
				continue;
			}
			
			
			if (!prevEndTime.isBefore(currStartTime)) {
				throw new DomainObjectException("予定時間の範囲が重複しています");
			}
		}
		
		return new DayTaskGroupCollection(taskDate, filteredTaskGroupList);
	}
	
	 /** タスク日付取得 */
	 public LocalDate getTaskDate() { 
		 return this.taskDate;
	}
	  
	/** タスクグループ一覧取得 */
	 public List<TaskGroup> getTaskGroups() { 
		 return List.copyOf(this.taskGroups); 
		 }
	 
	 /** 1日の合計予定時間（plannedTime のみ） */
	  public Duration totalPlanned() {
	    return this.taskGroups.stream()
	        .map( tg -> tg.plannedTime().orElse(null))
	        .filter(pt -> pt != null && pt.startTime() != null && pt.endTime() != null)
	        .map(ts -> Duration.between(ts.startTime(), ts.endTime()))
	        .reduce(Duration.ZERO, Duration::plus);
	  }
	  
	/**
	 * 必須チェック
	 * @param <T>
	 * @param obj 対象オブジェクト
	 * @param message エラーメッセージ
	 * @return 対象オブジェクト
	 */
	private static <T> T requireNonNull(T obj, String message) {
	    return Optional.ofNullable(obj)
	        .orElseThrow(() -> new DomainObjectException(message));
	}



}
