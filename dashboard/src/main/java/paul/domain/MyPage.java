package paul.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;


@Entity
@Table(name="MyPage_table")
@Data
public class MyPage {

        @Id
        //@GeneratedValue(strategy=GenerationType.AUTO)
        private Long orderId;
        private Long deliveryId;
        private String orderstatus;
        private String deliverystatus;


}
