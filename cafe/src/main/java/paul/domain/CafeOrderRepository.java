package paul.domain;

import paul.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="cafeOrders", path="cafeOrders")
public interface CafeOrderRepository extends PagingAndSortingRepository<CafeOrder, Long>{

}
