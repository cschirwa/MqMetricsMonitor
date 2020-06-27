package za.co.kemtech.metrics.monitor.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@RepositoryRestResource(collectionResourceRel = "thresholds", path = "thresholds")
public interface ThresholdRepository extends PagingAndSortingRepository<Threshold, Long> {

    Threshold findByUuid(@Param("uuid") final String uuid);

}

