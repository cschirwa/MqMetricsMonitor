package za.co.kemtech.metrics.monitor.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "breaches", path = "breaches")
public interface BreachRepository extends PagingAndSortingRepository<Breach, Long> {

}

