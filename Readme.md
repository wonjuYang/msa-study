# MSA-STUDY BY 코드프레소

## BOOK
스프링 마이크로서비스 코딩 공작소




### 220621
3장 config 4장 eureka 실습 파일 초안 버전 업데이트


### 220621
3장 config 4장 eureka 실습 파일 정리
5장 hystrix 5장 zuul 실습 파일 정리


# 스터디 내용

# 2주차

## 설정 외부화 패턴

### 이론

Cofing Server를 통해 여러 설정들을 외부에서 관리함

서비스 인스턴스가 시작하는 시점에만 Config Server에서 정보 조회 ( 인스턴스 운영중에 Config server의 장애가 발생해도 OK )

### 구현

<파일 기반>

- ConfigServer 생성
- ConfigServer  pom.xml에 Configserver 의존성 추가
- Application.java에 EnableConfigServer 어노테이션 추가
- apllciation.yml 파일에 cloud-config 관련 설정 추가
- src/main/resources 하위에 설정할 어플리케이션 이름의 폴더 추가
- 생성된 폴더에 yml 파일 추가
- 설정정보 이동
- 어플레케이션 pom.xml에 config-client, starter-config 의존성 추가
- src/main/resources 하위에 bootstrap.yml 추가 후 cloud: config 설정 추가

<git 기반>

- apllciation.yml 파일에 cloud-config 관련 설정 추가했던 걸 git으로 수정

## 서비스 디스커버리 패턴

### 이론

넷플릭스 유레카로부터 파생

대규모 분산 환경에서 서비스 디스커버리를 지원

서비스들이 Eureka Client가 되고 실행 시점에 Eureka Server에 자기 자신의 정보를 등록

서비스가 생성되면 자신의 정보를 에이전트에 등록하며 클라이언트들이 에이전트를 이용해 서비스 위치 검색이 가능하고 에이전트들끼리 클러스팅 하여 각자의 정보를 다른 서비스에 공유!

특징

1. 고가용성 : 서비스의 정보를 여러개의 노드가 공유
2. 피어 투 피어 : 모든 노드들이 서비스들의 상태를 공유
3. 장애 내성 : 에이전트는 인스턴스의 비정상 상태를 감지하고 제거함 → 사람 개입 없이
4. 회복성 : 클라이언트는 서비스의 정보를 로컬에 캐스(Ribbon)
5. 부하 분산 : 클라이언트의 요청을 후방 서비스들에게 분산해서 전달

Ribbon!

주기적으로 에이전트에 질의하여 에이전트로부터 받은 정보를 로컬에 캐싱→ 캐싱한 정보로 서비스를 호출

client-side load balancing 기능 수행

### 구현

- EurekaServer 생성
- pom.xml에 netflix-eureka-server 의존성 추가
- Application.java에 @EnableEurekaServer 어노테이션 추가
- application.yml에 eureka: client: 설정 정보 추가
- 서비스로 돌아가서 pom.xml에 netflix-eureka-clinet 추가
- application.yml 에 eureka: client 관련 설정 추가
- localhost:유레카서버포트/eureka/apps에서 확인

서비스 디스커버리를 이용하면 타 서비스 인스턴스의 물리적인 위치를 몰라도 됨!

Ribbon이 활성화된 RestTemplate 방법으로 적용

- 서비스 Application.java에 @LoadBalanced 어노테이션 추가
- restTemplate를 쓰는 곳에 가서 eureka에 등록된 서비스 이름으로 변경
- ex) localhost:8080→서비스이름




# 3주차

들어가기 전

넷플릭스 프로젝트들이 다 바뀌고 있다

Netflix Hystrix → Spring Cloud Circuit Breaker

Netflix Zuul → Spring Clod Gateway

기능 자체는 다 비슷하다

Spring Cloud Circuit Breaker 에는 리트라이 기능 정도 추가되었다

## 회로 차단기 패턴

### 이론

분산 시스템의 장애 

장애가 생긴 애를 지속적으로호출하지 않게 하자~가 목표!

장애가 발생한 서비스를 계속 호출하면 클라이언트도 망가질 수도 있다 

전체의 장애보다는 일부의 장애만 나는게 낫다

ex) 네이버 블로그는 안되더라도 네이버 카페는 될 수 있도록 

Circuit Breaker

원격 서비스에 대한 호출 정보를 집계하여 비정상 서비스를 반복적으로 호출하지 않도록 제어 → 특정 원격 자원에 대한 호출이 정해진만큼 실패할 경우 해당 원격 자원에 대해 호출하지 않도록 차단

fallback 

Circuit 차단 시 예외를 발생 시키는 대신 대체 행동을 정의

다른 데이터베이스를 호출하거나 향후 처리를 위해 Retry queue에 넣거나 하드 코딩된 결과를 응답하도록 설계

SG 회사 다닐 때 이런 패턴을 적용한 적이 있는데 추천 시스템 → 딥러닝 모델을 만들면 다른 팀에 서빙하는 일 커뮤니티 글 추천 

UI → 커뮤니티 → 추천서비스 → 딥러닝모듈 순서로 호출, 딥러닝이 굉장히 느렸음

그래서 추천시스템에서 hystrix를 붙이기로 함 5초 이상 걸리면 fallback 패턴을 둬서

개인화 된 맞춤 컨텐츠가 아닌 인기 컨텐츠를 추천할 수 있는 시스템을 붙임 

bulkhead 

원격 자원에 대한 호출을 자원 별 스레드 풀로 격리하여 관리

### 구현

- msa구성중인 프로젝트에 netflix-hystrix 의존성 추가
- Application.java에 어노테이션 추가(EnableHystrix)
- Circuit Breaker를 적용할 함수에 어노테이션 추가(HystrixCommand)
- yml 파일에 타임아웃, 실패 확률, 최소 호출 수 등을 Setting
- fallback method 구현(기존 method와 리턴타입, 파라미터가 동일해야함)

---

---

---

## 서비스 게이트웨이 패턴

### 이론

MSA의 각 서비스마다 동일하게 적용되야 하는 기능들(보안, 로깅, 분산 추적) 등이 존재하기 때문에 공통 라이브러리를 만들어서 사용하면 좋지 않을까? 하는 생각으로 나온 개념

즉! 필터단의 역활들을 각자 만들지 말고 하나의 곳으로 모여서 하자! 중앙화하자 → 다시 단점이 된다!  공통 라이브러리가 수정되면 모든 서비스들이 영향이 맞기 때문

Cross Cutting Concerns를 서비스들과 독립적으로 구성

한 정책 지점을 만들어 모든 호출이 해당 지점을 통한 후 각 서비스로 라우팅되도록하는 역할

### 구현

- 신규 springboot 프로젝트 구성
- Zuul 메이븐 의존성 추가 (netflix-zuul, eureka-client)
- [Application.java](http://Application.java) 에 Zuul 어노테이션 설정
- Eureka와 연동
- 라우팅 설정




