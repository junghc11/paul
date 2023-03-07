package paul.domain;

import paul.domain.OrderApproved;
import paul.domain.MakeStarted;
import paul.domain.MakeCompleted;
import paul.domain.OrderRejected;
import paul.CafeApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="CafeOrder_table")
@Data

public class CafeOrder  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private Long customerid;
    
    
    
    
    
    private Long cafeId;
    
    
    
    
    
    private Long menuId;
    
    
    
    
    
    private Integer qty;
    
    
    
    
    
    private Date orderDate;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private Long orderId;

    @PostPersist
    public void onPostPersist(){


        OrderApproved orderApproved = new OrderApproved(this);
        orderApproved.publishAfterCommit();



        MakeStarted makeStarted = new MakeStarted(this);
        makeStarted.publishAfterCommit();



        MakeCompleted makeCompleted = new MakeCompleted(this);
        makeCompleted.publishAfterCommit();



        OrderRejected orderRejected = new OrderRejected(this);
        orderRejected.publishAfterCommit();

    }

    public static CafeOrderRepository repository(){
        CafeOrderRepository cafeOrderRepository = CafeApplication.applicationContext.getBean(CafeOrderRepository.class);
        return cafeOrderRepository;
    }




    public static void addCafeOrder(Paid paid){

        /** Example 1:  new item 
        CafeOrder cafeOrder = new CafeOrder();
        repository().save(cafeOrder);

        */

        /** Example 2:  finding and process
        
        repository().findById(paid.get???()).ifPresent(cafeOrder->{
            
            cafeOrder // do something
            repository().save(cafeOrder);


         });
        */

        
    }
    public static void changeCafeOrder(PaymentCanceled paymentCanceled){

        /** Example 1:  new item 
        CafeOrder cafeOrder = new CafeOrder();
        repository().save(cafeOrder);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCanceled.get???()).ifPresent(cafeOrder->{
            
            cafeOrder // do something
            repository().save(cafeOrder);


         });
        */

        
    }


}
