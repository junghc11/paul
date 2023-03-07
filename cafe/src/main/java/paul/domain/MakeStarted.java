package paul.domain;

import paul.domain.*;
import paul.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class MakeStarted extends AbstractEvent {

    private Long id;
    private Long customerid;
    private Long cafeId;
    private Long menuId;
    private Integer qty;
    private Date orderDate;
    private String status;
    private Long orderId;

    public MakeStarted(CafeOrder aggregate){
        super(aggregate);
    }
    public MakeStarted(){
        super();
    }
}
