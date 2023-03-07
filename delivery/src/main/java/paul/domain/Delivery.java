package paul.domain;

import paul.domain.DeliveryConfirmed;
import paul.domain.DeliveryStarted;
import paul.DeliveryApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Delivery_table")
@Data

public class Delivery  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private Long orderId;
    
    
    
    
    
    private String address;
    
    
    
    
    
    private Long cafeId;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private Integer qty;
    
    
    
    
    
    private Date orderDate;

    @PostPersist
    public void onPostPersist(){


        DeliveryConfirmed deliveryConfirmed = new DeliveryConfirmed(this);
        deliveryConfirmed.publishAfterCommit();



        DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        deliveryStarted.publishAfterCommit();

    }

    public static DeliveryRepository repository(){
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(DeliveryRepository.class);
        return deliveryRepository;
    }




    public static void loadDeliveryInfo(MakeStarted makeStarted){

        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        */

        /** Example 2:  finding and process
        
        repository().findById(makeStarted.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);


         });
        */

        
    }


}
