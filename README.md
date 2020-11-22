# Kakao Money Spread   

## 개발 환경
|언어 및 도구|version 및 환경|
|---|---|
|java|jdk1.8|
|java 개발 Tool|STS4|
|WAS|Tomcat8|
|DBMS|Mariadb 5.5.68|
|DBMS Tool|HeidiSQL 11.0|
|Test Tool|POSTMAN|

## 사전 환경 세팅   
* DBMS 구성
1. 아래의 스크립트를 수행해서 Database, Table, Sequence 을 생성한다.   
   - /kakaopay/src/main/resources/mariadb/initDB.sql   
   
2. 구성물은 아래와 같다.   

|구분|이름|설명|
|---|---|---|
|Database|paydb|Pay 뿌리기 Database|
|Table|USER|사용자 관리 테이블|
|Table|ROOM|채팅방, 참여자 관리 테이블|
|Table|SPREAD|Money를 뿌린 기본 테이블|
|Table|SPREAD_FRIEND|뿌린 돈을 수령할 수 있는 친구 목록|
|Table|SEQUENCE|일련번호를 관리하기 위한 테이블|
|Function|Sequence를 증가 시키기 위한 함수|   

3. Test를 위한 Postman 스크립트
   - https://www.getpostman.com/collections/0b94d06da80134c2ceab   

## 고려 사항

## API 설명

## 미진한 점

