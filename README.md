# Simple Verified Member

## 사용 기술
* Language: Kotlin 1.6
* target JDK: JDK 17
* SpringBoot MVC
* H2 database(회원정보), redis(인증정보)
* Spring Data JPA, Spring Data Redis
* kotest, mockkito
* JWT

## 실행 방법
* docker로 구동하는 redis 필요
```
docker image pull redis
```
* docker desktop 실행
* redis 서버 구동
```
docker-compose -f docker-compose.redis.yml up -d
```
* redis 실행 후 application 실행
```
./gradlew bootRun
```
* application 종료 후 다시 시작 시, redis key 삭제 필요(application 종료 시 회원정보 휘발)
```
docker exec -it docker-redis redis-cli
redis-cli FLUSHALL
```

## Test 실행
```
./gradlew test
```

## Test 영역
* Controller, Controller advice unit test
* Domain layer use case test with FakeRepository
* Infra layer(Spring Data Redis, Spring Data JPA, Token 발행, 비밀번호 Hashing) 

## Happy path 구동
* happypath.http 참조 
* 인증코드값은 console 에 찍는 것으로 대체합니다. 아래와 같은 형태로 찍힙니다.
```
 receiver: 01089764629 // contents: 330733
```

## 구현 범위
* 요구사항 전체
  * 가입을 위한 전화번호 인증 요청 
    * POST /v1/verification/join
  * 인증코드 입력을 통한 가입 유효인증 생성 
    * PATCH /v1/verification
  * 인증된 전화번호로 가입 
    * POST /v1/members
  * 유일성이 검증된 이메일, 별명, 전화번호 그리고 비밀번호로 로그인 
    * POST /v1/auth/login
  * 내 정보 보기 
    * GET /v1/members/me
  * 비밀번호 초기화 위한 전화번호 인증 요청 
    * PATCH /v1/members/password-reset
  * 인증코드 입력을 통한 비밀번호변경 유효인증 생성 
    * POST /v1/verification/reset

## 개발적으로 신경 쓴 부분 
* Clean Architecture
* Domain Entity <-> Infra Entity Segregation (순수 도메인 객체)
* 불변객체 적극 활용
* Use Case 기반 
* 영역간 의존성을 domain 을 바라보는 방향으로 (domain은 다른 영역으로의 의존성 없도록)
  * request 영역 -> domain 
  * infra 영역 -> domain
* 로그인 시 JWT 발행, Interceptor를 통해 로그인 처리

## 주요 비즈니스 로직 
* 전화번호 인증 요청 후 3분간 인증코드 입력 가능 
  * 3분 안에 다시 인증 요청 할 경우, 기존 진행중인 인증 있음을 알림 
  * 인증이 성공적으로 된 경우 10분간 유효
  * 10분이 지난 후 해당 전화번호로의 요청 시 인증유효기한 만료 알림
* 인증 정보를 계속 가질 필요가 없을 것으로 생각하여 TTL 15분 부여

## 기타 유효성 검증
* 유저 입력값 검증 
* 유일성 검증되는 값은 이메일, 별명, 전화번호
* 동일 번호 중복 회원 가입 방지 

## 회고
* kotest 더 잘 써보고 싶습니다.
* spring security는 학습이 필요
* 하다보니 자꾸 커지는 프로젝트

## TODO DEVELOPMENT?
* 요청 파라미터 검증 강화 
* 인증코드를 3분간 000000 - 999999 까지 100만건 요청하면? --> count 방어로직 필요
* 에러처리 코드화
* 기타 할 것은 항상 많..
