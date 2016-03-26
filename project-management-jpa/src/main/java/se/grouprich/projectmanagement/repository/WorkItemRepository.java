package se.grouprich.projectmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.WorkItemData;
import se.grouprich.projectmanagement.status.WorkItemStatus;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItemData, Long>
{
	@Transactional
	List<WorkItemData> removeById(Long id);

	List<WorkItemData> findByStatus(WorkItemStatus status);

	List<WorkItemData> findByDescriptionContaining(String keyword);

	@Query("SELECT w FROM #{#entityName} w WHERE w.user.team = ?1")
	List<WorkItemData> findByTeam(TeamData team);

	List<WorkItemData> findByUser(UserData user);
}
