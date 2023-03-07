package paul.infra;

import javax.naming.NameParser;

import javax.naming.NameParser;
import javax.transaction.Transactional;

import paul.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import paul.domain.*;

@Service
@Transactional
public class PolicyHandler{
    @Autowired CafeOrderRepository cafeOrderRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='Paid'")
    public void wheneverPaid_AddCafeOrder(@Payload Paid paid){

        Paid event = paid;
        System.out.println("\n\n##### listener AddCafeOrder : " + paid + "\n\n");


        

        // Sample Logic //
        CafeOrder.addCafeOrder(event);
        

        

    }

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentCanceled'")
    public void wheneverPaymentCanceled_ChangeCafeOrder(@Payload PaymentCanceled paymentCanceled){

        PaymentCanceled event = paymentCanceled;
        System.out.println("\n\n##### listener ChangeCafeOrder : " + paymentCanceled + "\n\n");


        

        // Sample Logic //
        CafeOrder.changeCafeOrder(event);
        

        

    }

}


