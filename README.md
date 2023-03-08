![image](https://user-images.githubusercontent.com/74826215/223622573-326ce345-d9bc-4ade-b2c8-fb4a3e479f85.png)


# 예제 - 모바일 음료주문


시나리오
 [기능적 요구사항]
1. 고객이 모바일 앱을 통해 지점, 메뉴를 선택한다
1. 고객이 선택한 메뉴에 대해 결제함으로써 주문이 발생한다.
1. 주문이 되면 주문 내역이 상점주인에게 전달된다
1. 상점주는 수문을 수락하거나 거절할 수 있다.
1. 상점주인이 주문을 확인하고 음료를 제조하기 시작한다
1. 음료를 만들기 시작하면 고객이 대기 순서를 확인할 수 있다.
1. 음료를 만들기 시작하면 배달의 경우 고객 지역 인근의 라이더들에 의해 배송건 조회가 가능하다.
1. 고객이 주문을 취소할 수 있다
1. 주문이 취소되면 배달이 취소된다
1. 고객이 주문상태를 중간중간 조회한다
1. 라이더가 음료를 전달한 뒤 배송확인 버튼을 누르면 모든 거래가 완료된다.
1. 주문 진행상황을 모바일 메신저로 공유한다

[비기능적 요구사항]
1. 주문이 확정되어 음료가 제조되면 취소 할 수 없어야 한다.

1. 트랜잭션
   1. 결제가 되지 않은 주문건은 아예 거래가 성립되지 않아야 한다 Sync 호출
1. 장애격리
   1. 상점관리 기능이 수행되지 않더라도 주문은 365일 24시간 받을 수 있어야 한다 Async (event-driven), Eventual Consistency
   1. 고객이 자주 상점관리에서 확인할 수 있는 배달상태를 주문시스템(프론트엔드)에서 확인할 수 있어야 한다 CQRS


## 체크포인트
## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684144-2a893200-826a-11ea-9a01-79927d3a0107.png)

## TO-BE 조직 (Vertically-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684159-3543c700-826a-11ea-8d5f-a3fc0c4cad87.png)



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


## Self-healing (liveness probe)
Order 컨테이너에 장애가 생겼을 때, 컨테이너 플랫폼이 자동으로 장애를 감지하여 복구하도록 설정합니다.
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order
  labels:
    app: order
spec:
  replicas: 1
  selector:
    matchLabels:
      app: order
  template:
    metadata:
      labels:
        app: order
    spec:
      containers:
        - name: order
          image: sjjo0319/order:230307
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: "200m"
          readinessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 20
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10 
          livenessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 30
            timeoutSeconds: 2
            successThreshold: 1
            periodSeconds: 1
            failureThreshold: 5
          volumeMounts:
          - mountPath: "/mnt/data"
            name: volume
      volumes:
        - name: volume
          persistentVolumeClaim:
            claimName: aws-efs
```
- Liveness Probe를 확인합니다
![image](https://user-images.githubusercontent.com/74826215/223605978-3477d637-33cf-48fd-9335-fce7eab138c0.png)





## Apply Service Mesh

- istio를 설치하고 Kiali와 Jaeger를 기동합니다.
![image](https://user-images.githubusercontent.com/74826215/223606436-1c516913-2edb-434f-a589-0743df206b5d.png)
![image](https://user-images.githubusercontent.com/74826215/223606536-fb1bc43d-0c61-4185-be05-039ca0ec825a.png)


- istio를 설치하고 각 Pod에 SideCar를 Inject 합니다.
사용자 트래픽의 흐름이나 설정된 istio 구성요소들의 동작 상황을 실시간 감지하여 그래픽을 통해 모니터링 합니다.
![image](https://user-images.githubusercontent.com/74826215/223606246-b0439666-50d2-467d-b4f7-ddd83d6c8f36.png)


Loggregation / Monitoring
