package paul.domain;

import paul.domain.OrderPlaced;
import paul.domain.OrderCanceled;
import paul.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Order_table")
@Data

public class Order  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private Long menuId;
    
    
    
    
    
    private Integer qty;
    
    
    
    
    
    private Long totalPrice;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private Date orderDate;
    
    
    
    
    
    private Long cafeId;
    
    
    
    
    
    private Long customerId;

    @PostPersist
    public void onPostPersist(){


        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();



        OrderCanceled orderCanceled = new OrderCanceled(this);
        orderCanceled.publishAfterCommit();

    }

    public static OrderRepository repository(){
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }






}
