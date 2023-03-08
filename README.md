![image](https://user-images.githubusercontent.com/74826215/223622573-326ce345-d9bc-4ade-b2c8-fb4a3e479f85.png)


# 예제 - 모바일 음료주문


## 시나리오
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

## Event Storming 결과  
   MSAEz 로 모델링한 이벤트스토밍 결과:   
   https://labs.msaez.io/#/storming/fc605fea1fa7b66445d0a7dff5f51e34   
   ![image](https://user-images.githubusercontent.com/121933672/223629043-bf4842c4-dadc-4b23-a3c9-ce44fc8a6207.png)


## 체크포인트
## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684144-2a893200-826a-11ea-9a01-79927d3a0107.png)

## TO-BE 조직 (Vertically-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684159-3543c700-826a-11ea-8d5f-a3fc0c4cad87.png)


## Saga (Pub-Sub)  
Order 서비스에서 OrderPlaced 이벤트를 발행하면, Payment 서비스에서 OrderPlaced 이벤트를 수신하여 StartPayment 작업을 실행한다. 
StartPayment 작업은 Paid 이벤트를 발행하고, Café 서비스에서 Paid 이벤트를 수신하여 AddCafeOrder 작업을 실행한다.

Order 서비스를 호출하여 주문 요청시 OrderPlaced, Paid 토픽이 발행되며, 주문 이력이 cafe repository에 저장되는 것을 확인할 수 있다.

[주문 요청]
![image](https://user-images.githubusercontent.com/74826215/223603363-8de0a839-df63-4150-82aa-f01f20d2427e.png)


[이벤트 발행 확인]
![image](https://user-images.githubusercontent.com/74826215/223603384-0ff9116a-beb7-44ed-9d60-330a85609c6b.png)


[주문내역(café repository) 확인]
![image](https://user-images.githubusercontent.com/74826215/223603397-5be2659c-40b3-408d-b0f8-c73a016f16ae.png)


## CQRS  
주문 및 배달 현황을 확인할 수 있는 마이페이지 서비스를 위해 Read Model 생성
[MyPage]
 ![image](https://user-images.githubusercontent.com/74826215/223603407-2bf0968e-ce03-457e-84dc-feb7a10c86a5.png)
![image](https://user-images.githubusercontent.com/74826215/223603421-6a3617ca-3e65-48bb-83b7-c2241f33297d.png)
![image](https://user-images.githubusercontent.com/74826215/223603443-4a19306d-d236-490a-af5f-4cf141d2b083.png)

 

 
주문 요청 후 CQRS에 정의된 내용에 따라 주문/배달 상태가 변경되었음

## Compensation & Correlation  


## Gateway


## Deploy
```
cd order
mvn package -B -Dmaven.test.skip=true
java -jar target/order-0.0.1-SNAPSHOT.jar
```

- 작성한 패키징을 도커라이징 시킨다. 

```
docker login
docker build -t sjjo0319/order:230307 .     
docker images
docker push sjjo0319/order:230307 
```

- 생성된 이미지를 yaml 이용하여 K8S에  Deploy 한다
```
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
```

- Order(주문서비스)의 Deployment.yaml 
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

- Order Command를 요청하고 상태를 확인한다. 

![image](https://user-images.githubusercontent.com/74826215/223631433-1229defe-e873-46a4-a7b5-3714e7d73447.png)




## Autoscale (HPA)
- 주문 요청이 많아질 경우 order pod를 확장하여 요청을 처리한다.

siege pod를 생성하여 부하 테스트
- siege -c20 -t40S -v http://order:8080/orders
![image](https://user-images.githubusercontent.com/20621385/223629663-bec2c789-2b56-4a97-a942-b53f9f887be3.png)
![image](https://user-images.githubusercontent.com/20621385/223630157-568f99f5-e1e7-4a81-9f67-c77d1b421453.png)

pod 생성 확인
![image](https://user-images.githubusercontent.com/20621385/223629217-e10a0e74-07d8-4c63-8f44-b003ce96e01f.png)



## Zero-downtime deploy (Readiness probe)
- seige 로 배포작업 직전에 워크로드를 모니터링 함.
![image](https://user-images.githubusercontent.com/121846555/223623164-401cb0c6-83e6-4775-ae04-22be46a768ea.png)

-	Readiness probe 적용 전

    ![image](https://user-images.githubusercontent.com/121846555/223623199-4515f93f-1f1d-4f88-921e-74849aba60da.png)

    : Availability가 100% 미만으로 떨어졌으므로 정지시간이 발생한 것이 확인됨.

-	Readiness probe 설정 (deployment.yaml)

    ![image](https://user-images.githubusercontent.com/121846555/223623216-b85b9343-3a6d-4b1d-868c-b78d1fc141a9.png)

- 수정된 주문 서비스 배포
![image](https://user-images.githubusercontent.com/121846555/223623236-8d8d7821-62e6-400f-8e22-c718bcd383d3.png)

    ![image](https://user-images.githubusercontent.com/121846555/223623251-aa3d4763-3926-4fbf-8b33-5a49b36b982a.png) 

    : 배포기간 동안 Availability의 변화가 없기 때문에 무정지 재배포가 성공한 것으로 확인됨.

## Persistence Volume/ConfigMap/Secret  
  EFS (Elastic File System) 사용을 위한 설정
  - Step. 1: EFS 생성 
    ![image](https://user-images.githubusercontent.com/121933672/223619655-38b0a7c7-d6cc-4d75-816d-9cdcd55ffc0f.png)
    ![image](https://user-images.githubusercontent.com/121933672/223619692-0fff160c-df69-4d84-bf55-35ad34b6241a.png)

  - Step. 2: EFS계정 생성 및 Role 바인딩 
     ServerAccount 생성   
     서비스 계정(efs-provisioner)에 권한(rbac) 설정   

  - Step. 3: EFS Provisioner 설치   
     efs-provisioner.yaml 편집하여 EKS에 EFS 프로비저너 설치  
        value: # fs-0955d6fce0c755475 => 파일 시스템 ID  
        value: # eu-central-1 => EKS 리전  
        server:# fs-0955d6fce0c755475.efs.eu-central-1.amazonaws.com => 파일 시스템 ID  
        ![image](https://user-images.githubusercontent.com/121933672/223622035-238f0a8d-afa3-4ab7-b64d-406339bdcf13.png)

  - Step. 4: StorageClass 생성 
        ![image](https://user-images.githubusercontent.com/121933672/223622305-4976882b-0b5e-4798-8e21-79a1113f6f1d.png)

  - Step. 5: PVC 생성   
    ![image](https://user-images.githubusercontent.com/121933672/223622259-9912f848-a9a1-48bc-a11f-d9df95989d72.png)
    정상적으로 mount되었는지 확인   
    ![image](https://user-images.githubusercontent.com/121933672/223622343-5a5300d9-327a-46d1-aa39-3bb594396a16.png)



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



## Loggreagation / Monitoring

- 로그를 모니터링하기위해 Kibana 설치하여 LoadBalancer 타입 확인.
  ![image](https://user-images.githubusercontent.com/121846555/223625288-ed056165-1e71-42ad-a18f-f336f5e6aacd.png)

- 접속 주소: 
  http://a92298cab2ac94027be92659f08c9479-2039032239.eu-central-1.elb.amazonaws.com:5601

- Kibana Web Admin의 Analytics > Discover에 접속하여 로그 조회
  ![image](https://user-images.githubusercontent.com/121846555/223625313-441c7ff1-2303-4a6d-87bc-8f62dd959680.png)

  : 전체 로그가 조회됨.

-	‘Add filter’에서 'kubernetes.container_name is order’로 조건을 지정
  ![image](https://user-images.githubusercontent.com/121846555/223625343-59d8559f-b080-4b48-8b75-c377ae17cca7.png)

  : 지정한 조건의 로그만 조회됨.
