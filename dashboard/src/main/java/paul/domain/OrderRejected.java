package paul.domain;

import paul.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class OrderRejected extends AbstractEvent {

    private Long id;
    private Long customerid;
    private Long cafeId;
    private Long menuId;
    private Integer qty;
    private Date orderDate;
    private String status;
    private Long orderId;
}
