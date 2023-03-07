package paul.domain;

import paul.domain.*;
import paul.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class PaymentCanceled extends AbstractEvent {

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


