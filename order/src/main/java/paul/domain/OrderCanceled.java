package paul.domain;

import paul.domain.*;
import paul.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class OrderCanceled extends AbstractEvent {

    private Long id;
    private Long menuId;
    private Integer qty;
    private Long totalPrice;
    private String status;
    private Date orderDate;
    private Long cafeId;
    private Long customerId;

    public OrderCanceled(Order aggregate){
        super(aggregate);
    }
    public OrderCanceled(){
        super();
    }
}
