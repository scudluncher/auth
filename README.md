# Simple Verified Member

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

## 사용 기술
* Language: Kotlin 1.6
* target JDK: JDK 17
* SpringBoot MVC
* H2 database(회원정보), redis(인증정보)
* Spring Data JPA, Spring Data Redis
* kotest, mockkito
* JWT
* Clean Architecture
* Domain Entity <-> Infra Entity Segregation

## Happy path 구동
* happypath.http 참조 
