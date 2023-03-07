package paul.infra;

import paul.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel="myPages", path="myPages")
public interface MyPageRepository extends PagingAndSortingRepository<MyPage, Long> {

    List<MyPage> findByOrderId(Long orderId);
List<MyPage> findByDeliveryId(Long deliveryId);


    
}
