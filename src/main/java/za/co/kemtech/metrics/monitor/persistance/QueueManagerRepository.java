package za.co.kemtech.metrics.monitor.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "queue-managers", path = "queue-managers")
public interface QueueManagerRepository extends PagingAndSortingRepository<QueueManager, Long> {

    QueueManager findByHostnameAndQueueManagerName(@Param("hostname") final String hostname, @Param("queueManagerName") final String queueManagerName);

    @Override
    List<QueueManager> findAll();
}

