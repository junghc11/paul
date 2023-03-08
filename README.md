1. Saga (Pub-Sub)
Order 서비스에서 OrderPlaced 이벤트를 발행하면, Payment 서비스에서 OrderPlaced 이벤트를 수신하여 StartPayment 작업을 실행한다. 
StartPayment 작업은 Paid 이벤트를 발행하고, Café 서비스에서 Paid 이벤트를 수신하여 AddCafeOrder 작업을 실행한다.

Order 서비스를 호출하여 주문 요청시 OrderPlaced, Paid 토픽이 발행되며, 주문 이력이 cafe repository에 저장되는 것을 확인할 수 있다.

[주문 요청]
![image](https://user-images.githubusercontent.com/74826215/223603363-8de0a839-df63-4150-82aa-f01f20d2427e.png)


[이벤트 발행 확인]
![image](https://user-images.githubusercontent.com/74826215/223603384-0ff9116a-beb7-44ed-9d60-330a85609c6b.png)


[주문내역(café repository) 확인]
![image](https://user-images.githubusercontent.com/74826215/223603397-5be2659c-40b3-408d-b0f8-c73a016f16ae.png)


2. CQRS
주문 및 배달 현황을 확인할 수 있는 마이페이지 서비스를 위해 Read Model 생성
[MyPage]
 ![image](https://user-images.githubusercontent.com/74826215/223603407-2bf0968e-ce03-457e-84dc-feb7a10c86a5.png)
![image](https://user-images.githubusercontent.com/74826215/223603421-6a3617ca-3e65-48bb-83b7-c2241f33297d.png)
![image](https://user-images.githubusercontent.com/74826215/223603443-4a19306d-d236-490a-af5f-4cf141d2b083.png)

 

 
주문 요청 후 CQRS에 정의된 내용에 따라 주문/배달 상태가 변경되었음

3. 

CQRS
Compensation & Correlation
Request-Response (Not implemented)
Circuit Breaker (Not implemented)
Gateway / Ingress
Deploy / Pipeline
Autoscale (HPA)
Zero-downtime deploy (Readiness probe)
Persistence Volume/ConfigMap/Secret
Self-healing (liveness probe)
Apply Service Mesh
Loggregation / Monitoring
