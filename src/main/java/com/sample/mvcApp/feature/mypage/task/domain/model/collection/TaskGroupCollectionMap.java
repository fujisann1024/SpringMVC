package com.sample.mvcApp.feature.mypage.task.domain.model.collection;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;

/**
 * タスクグループコレクションマップ
 * 
 * <pre>
 * 日付ごとにタスクグループコレクションを管理するマップ
 * </pre>
 */
public final class TaskGroupCollectionMap {

	/** 日付別タスクグループコレクションマップ */
	private final Map<LocalDate, DayTaskGroupCollection> byDate;

	/**
	 * コンストラクタ
	 * @param normalized
	 */
	private TaskGroupCollectionMap(Map<LocalDate, DayTaskGroupCollection> normalized) {
		this.byDate = Map.copyOf(normalized); // 不変
	}

	public Map<LocalDate, DayTaskGroupCollection> getByDate() {
		return byDate;
	}

	/**
	 * ファクトリーメソッド
	 * 
	 * <pre>
	 * 指定されたタスクグループ一覧からタスクグループコレクションを生成する。
	 * </pre>
	 * 
	 * @param all タスクグループ一覧
	 * @return タスクグループコレクション
	 * @throws DomainObjectException 不正な値オブジェクトの場合
	 */
	public static TaskGroupCollectionMap of(List<TaskGroup> all) {
		requireNonNull(all, "TaskGroup list is required");

		// 日付でグルーピング
		Map<LocalDate, List<TaskGroup>> grouped = all.stream()
				.collect(Collectors.groupingBy(g -> g.id().workYmd()));

		// 各日ごとに DayTaskGroups を作成
		Map<LocalDate, DayTaskGroupCollection> map = new HashMap<>();
		for (Entry<LocalDate, List<TaskGroup>> e : grouped.entrySet()) {

			map.put(e.getKey(), DayTaskGroupCollection.of(e.getKey(), e.getValue()));
		}
		return new TaskGroupCollectionMap(map);
	}

	/** 期間の合計予定時間 */
	public Duration totalPlanned(LocalDate fromInclusive, LocalDate toInclusive) {
		return byDate.entrySet().stream()
				.filter(e -> !e.getKey().isBefore(fromInclusive) && !e.getKey().isAfter(toInclusive))
				.map(e -> e.getValue().totalPlanned())
				.reduce(Duration.ZERO, Duration::plus);
	}

	/** フラットなタスクグループ一覧として取得 */
	public List<TaskGroup> asFlatList() {

		return byDate.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.map(Map.Entry::getValue)
				.flatMap(d -> d.getTaskGroups().stream()).toList();
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
