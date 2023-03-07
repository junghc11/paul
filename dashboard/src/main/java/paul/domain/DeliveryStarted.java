package paul.domain;

import paul.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class DeliveryStarted extends AbstractEvent {

    private Long id;
    private Long orderId;
    private String address;
    private Long cafeId;
    private String status;
    private Integer qty;
    private Date orderDate;
}
