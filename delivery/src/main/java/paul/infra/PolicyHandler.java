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
    @Autowired DeliveryRepository deliveryRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='MakeStarted'")
    public void wheneverMakeStarted_LoadDeliveryInfo(@Payload MakeStarted makeStarted){

        MakeStarted event = makeStarted;
        System.out.println("\n\n##### listener LoadDeliveryInfo : " + makeStarted + "\n\n");


        

        // Sample Logic //
        Delivery.loadDeliveryInfo(event);
        

        

    }

}


