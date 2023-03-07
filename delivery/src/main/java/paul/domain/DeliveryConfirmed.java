package paul.domain;

import paul.domain.*;
import paul.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class DeliveryConfirmed extends AbstractEvent {

    private Long id;
    private Long orderId;
    private String address;
    private Long cafeId;
    private String status;
    private Integer qty;
    private Date orderDate;

    public DeliveryConfirmed(Delivery aggregate){
        super(aggregate);
    }
    public DeliveryConfirmed(){
        super();
    }
}
