package airbnb;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="rentcars", path="rentcars")
public interface RentcarRepository extends PagingAndSortingRepository<Rentcar, Long>{


}
