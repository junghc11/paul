package paul.domain;

import paul.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class Paid extends AbstractEvent {

    private Long id;
    private Long customerId;
    private Long orderId;
    private Long cafeId;
    private Integer qty;
    private Long totalPrice;
    private Date orderDate;
    private Long approvalCode;
    private String status;
}
