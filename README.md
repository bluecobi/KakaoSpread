# Kakao Money Spread   

## 개발 환경
|언어 및 도구|version 및 환경|
|---|---|
|java|jdk1.8|
|java 개발 Tool|STS4|
|WAS|Tomcat8|
|F/W|SpringBoot|
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
|Function|nextval|Sequence를 증가 시키기 위한 함수|   

3. Test를 위한 Postman 스크립트 (SharedLink)
   - https://www.getpostman.com/collections/0b94d06da80134c2ceab   

4. 환경 파일
   - Database 접속 정보 관리 : /Kakaopay/src/main/resources/static/property/config.properties

## API 설명 (HTTP Method는 POST로 통일)   
|기능|url|HEADER 정보 및 Sample|Parameter Sample|전송 Type|설명|
|---|---|---|---|---|---|
|Clean All Data|http://localhost:8080/openapi/clear/all|N/A|N/A|N/A|한꺼번에 초기화|
|Clean All User|http://localhost:8080/openapi/clear/allUser|N/A|N/A|N/A|개별 초기화 : 사용자|
|Clean All Room|http://localhost:8080/openapi/clear/allRoom|N/A|N/A|N/A|개별 초기화 : 채팅방, 참여자|
|Clean All Spread Info|http://localhost:8080/openapi/clear/allSpread|N/A|N/A|N/A|개별 초기화 : 뿌린 돈 정보와 대상 정보|
|Init Data|http://localhost:8080/openapi/init/all|X-ROOM-ID(RoomID-001)|{ "userCnt": 10, "amounts": 100000000 }|JSON|원하는 방 이름과 사용자 수 그리고 초기 보유 금액을 생성|
|Spread Money|http://localhost:8080/openapi/spreadMoney|X-USER-ID(0), X-ROOM-ID(RoomID-001)|{ "spreadAmounts": 1000, "friendCnt": 5 }|JSON|정상:Money를 뿌린다.|
|Spread Money|http://localhost:8080/openapi/spreadMoney|X-USER-ID(0), X-ROOM-ID(RoomID-001)|{ "spreadAmounts": -1000, "friendCnt": 5 }|JSON|오류1:마이너스 금액 뿌리기|
|Spread Money|http://localhost:8080/openapi/spreadMoney|X-USER-ID(0), X-ROOM-ID(RoomID-001)|{ "spreadAmounts": 1000, "friendCnt": -5 }|JSON|오류2:마이너스 대상자 뿌리기|
|Money Take|http://localhost:8080/openapi/takeMoney|X-USER-ID(1), X-ROOM-ID(RoomID-001)|{ "tokenId": "생성된 TokenID" }|JSON|정상|
|Money Take|상동|X-USER-ID(1), X-ROOM-ID(RoomID-001)|{ "tokenId": "생성된 TokenID" }|JSON|오류1:10분 이후 테스트|
|Money Take|상동|X-USER-ID(0), X-ROOM-ID(RoomID-001)|{ "tokenId": "생성된 TokenID" }|JSON|오류2:뿌린 당사자가 금액 취하기|
|Money Take|상동|X-USER-ID(2), X-ROOM-ID(RoomID-001)|{ "tokenId": "임의의 TokenID" }|JSON|오류3:Token이 정확하지 않을 경우|
|Money Take|상동|X-USER-ID(2), X-ROOM-ID(임의의 방)|{ "tokenId": "생성된 TokenID" }|JSON|오류4:참여한 방이 정확하지 않을 경우|
|Money Take|상동|X-USER-ID(999), X-ROOM-ID(RoomID-001)|{ "tokenId": "생성된 TokenID" }|JSON|오류5:권한이 없는 사용자 접근|
|Money Take|상동|X-USER-ID(3), X-ROOM-ID(RoomID-001)|{ "tokenId": "생성된 TokenID" }|JSON|오류6:뿌린 대상보다 많은 사람이 수령하려고 할 경우|
|View|http://localhost:8080/openapi/viewSpread|X-USER-ID(0)|{ "tokenId": "생성된 TokenID" }|JSON|정상:금액을 뿌린 당사자, 정확한 TokenId|
|View|상동|X-USER-ID(0)|{ "tokenId": "생성된 TokenID" }|JSON|오류1:기간 경과-7일 이후 테스트|
|View|상동|X-USER-ID(0)|{ "tokenId": "임의의 TokenID" }|JSON|오류2:Token이 정확하지 않을 경우|
|View|상동|X-USER-ID(2)|{ "tokenId": "생성된 TokenID" }|JSON|오류3:권한이 없는 사용자 요청|
|View|상동|X-USER-ID(999)|{ "tokenId": "생성된 TokenID" }|JSON|오류4:없는 사용자 요청|

## 핵심 문제 해결 전략   
1. 정보를 한곳에 집중시키지 않는다.
   - 테이블을 성격에 맞게 분리/관리 한다.
2. 쿼리를 단순화 한다.
   - SQL 조인을 최소화하고 java application에서 관리한다.
3. 예외 및 에러에 대한 코드 관리   
   - 장애 상황을 이해 할 수 있는 메시지를 관리한다.   
4. 다양한 오동작을 고려한 테스트 실시   

## 미진한 점
1. 에러 코드 관리 
   - 에러 코드를 DB로 관리하지 못함
2. JPA 적용하지 못함 
3. Cloud에 테스트 환경 구성 못함
