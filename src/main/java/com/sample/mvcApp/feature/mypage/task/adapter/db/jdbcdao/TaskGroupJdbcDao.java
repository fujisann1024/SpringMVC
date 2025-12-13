package com.sample.mvcApp.feature.mypage.task.adapter.db.jdbcdao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sample.mvcApp.common.exception.DomainObjectException;
import com.sample.mvcApp.feature.mypage.task.adapter.db.helper.TaskGroupDBHelper;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupDto;
import com.sample.mvcApp.feature.mypage.task.adapter.db.jpadto.TaskGroupKeyDto;
import com.sample.mvcApp.feature.mypage.task.domain.model.aggregate.TaskGroup;
import com.sample.mvcApp.feature.mypage.task.domain.model.collection.TaskGroupCollectionMap;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.TaskGroupId;
import com.sample.mvcApp.feature.mypage.task.domain.model.value.WeekRange;
import com.sample.mvcApp.feature.mypage.task.domain.port.TaskGroupQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@Repository
public class TaskGroupJdbcDao implements TaskGroupQuery {
	
	@PersistenceContext
    private EntityManager em;

	@Override
	public TaskGroupCollectionMap getTaskGroupWeekRange(WeekRange weekRange) {
		
		CriteriaBuilder  cb = em.getCriteriaBuilder();
		CriteriaQuery<TaskGroupDto> cq = cb.createQuery(TaskGroupDto.class);

        Root<TaskGroupDto> u = cq.from(TaskGroupDto.class);
        
        Path<LocalDate> workYmd = u.get("id").get("workYmd");
     
		cq.select(u)
		  .where(
			cb.between(
					workYmd
			  , weekRange.getStart()
			  , weekRange.getEnd())
			).orderBy(cb.asc(workYmd));  
		
		List<TaskGroupDto> resultList = em.createQuery(cq).getResultList();
		List<TaskGroup> taskGroupList = resultList.stream()
				.map(dto -> TaskGroupDBHelper.parseToTaskGroup(dto))
				.toList();
		
		return TaskGroupCollectionMap.of(taskGroupList);
		
	}

	@Override
	public TaskGroup getTaskGroupById(TaskGroupId taskGroupId) {
		TaskGroupKeyDto key = TaskGroupKeyDto.builder()
				.taskGroupId(taskGroupId.groupId())
				.workYmd(taskGroupId.workYmd())
				.build();
		TaskGroupDto dto = em.find(TaskGroupDto.class, key);

		if (dto == null) {
			throw new DomainObjectException("指定されたタスクが見つかりませんでした");
		}

		return TaskGroupDBHelper.parseToTaskGroup(dto);
	}
}
