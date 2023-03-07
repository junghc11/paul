package paul.infra;

import paul.domain.*;
import paul.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyPageViewHandler {

    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1 (@Payload OrderPlaced orderPlaced) {
        try {

            if (!orderPlaced.validate()) return;

            // view 객체 생성
            MyPage myPage = new MyPage();
            // view 객체에 이벤트의 Value 를 set 함
            myPage.setOrderId(orderPlaced.getId());
            myPage.setOrderstatus("주문됨");
            myPage.setDeliverystatus("배달전");
            // view 레파지 토리에 save
            myPageRepository.save(myPage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaid_then_UPDATE_1(@Payload Paid paid) {
        try {
            if (!paid.validate()) return;
                // view 객체 조회

                List<MyPage> myPageList = myPageRepository.findByOrderId(paid.getCafeOrderId());
                for(MyPage myPage : myPageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setOrderstatus("결제됨");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenMakeStarted_then_UPDATE_2(@Payload MakeStarted makeStarted) {
        try {
            if (!makeStarted.validate()) return;
                // view 객체 조회

                List<MyPage> myPageList = myPageRepository.findByOrderId(makeStarted.getOrderId());
                for(MyPage myPage : myPageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setOrderstatus("제조중");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenMakeCompleted_then_UPDATE_3(@Payload MakeCompleted makeCompleted) {
        try {
            if (!makeCompleted.validate()) return;
                // view 객체 조회

                List<MyPage> myPageList = myPageRepository.findByOrderId(makeCompleted.getOrderId());
                for(MyPage myPage : myPageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setOrderstatus("제조완료");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_4(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회

                List<MyPage> myPageList = myPageRepository.findByDeliveryId(deliveryStarted.getId());
                for(MyPage myPage : myPageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myPage.setDeliverystatus("배달중");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCanceled_then_UPDATE_5(@Payload OrderCanceled orderCanceled) {
        try {
            if (!orderCanceled.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findByOrderId(orderCanceled.getId());

            if( myPageOptional.isPresent()) {
                 MyPage myPage = myPageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setOrderstatus("취소됨");    
                // view 레파지 토리에 save
                 myPageRepository.save(myPage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryConfirmed_then_UPDATE_6(@Payload DeliveryConfirmed deliveryConfirmed) {
        try {
            if (!deliveryConfirmed.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findByOrderId(deliveryConfirmed.getId());

            if( myPageOptional.isPresent()) {
                 MyPage myPage = myPageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setDeliverystatus("배달완료");    
                // view 레파지 토리에 save
                 myPageRepository.save(myPage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderRejected_then_UPDATE_7(@Payload OrderRejected orderRejected) {
        try {
            if (!orderRejected.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findByOrderId(orderRejected.getOrderId());

            if( myPageOptional.isPresent()) {
                 MyPage myPage = myPageOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setOrderstatus("취소됨");    
                // view 레파지 토리에 save
                 myPageRepository.save(myPage);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

