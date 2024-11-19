# 🛒 웹 쇼핑 기능 구현 🛒

📆 제작 기간 : 2024.07 ~ 2024.09  
😶 제작자 : 윤진욱


# 🧩 프로젝트 소개  

책과 인터넷 강의 등으로 학습하여 웹 쇼핑 기능을 조금이라도 구현 해보려고 하는 것이 목표인 프로젝트(클론코딩) 입니다.

사용자는 두 부류로 나뉩니다.
1. 구매자 </br>
   구매자는 말 그대로 제품을 구매하려는 사람이고 제품 리스트에서 상세 페이지에 접근해 내용을 살펴보고 수량을 정해 구매를 진행합니다. </br>
   구매자를 위한 기능에는 회원가입(로그인/로그아웃), 제품 리스트 확인, 제품 검색, 제품 필터링, 제품 상세 보기, 제품 리뷰 작성, 제품 구매, 구매 리스트, 장바구니가 있습니다.
    
2. 판매자 </br>
   판매자는 제품을 등록해 판매하고 구매자가 주문을 했을 때 주문 승인과 동시에 배송을 지정할 수 있습니다. </br>
   판매자를 위한 기능에는 회원가입(로그인/로그아웃), 제품 리스트 확인, 제품 검색 및 필터링, 제품 등록, 제품 관리, 주문 리스트가 있습니다.

# 🔍 주요 기능
### 구매자 주문 목록
* 구매 내역 확인과 취소 기능
* http://localhost:8080/orders (구매자 로그인 시 이용 가능) </br>
### 판매자 주문 목록
* 판매자가 등록한 제품에 대한 주문 확인 및 배송 결정
* http://localhost:8080/seller/orders (판매자 로그인 시 이용 가능) </br>
### 리뷰
* 구매자 제품의 리뷰 등록 및 삭제 </br>
### 장바구니
* 제품 상세 페이지에서 구매할 제품을 담아 일괄 주문이 가능
* http://localhost:8080/cart (구매자 로그인 시 이용 가능) </br>
### 회원가입/로그인/로그아웃
* 세션을 통한 로그인 유지 및 로그아웃

# 👓 뷰
![1](https://github.com/user-attachments/assets/17935d08-8662-4613-97a1-dd873480d762)
![2](https://github.com/user-attachments/assets/69f09ab7-6c03-400f-95a7-678b21eeb79d)
![3](https://github.com/user-attachments/assets/dc357557-6a3d-4ba4-9241-8638cc705210)
![4](https://github.com/user-attachments/assets/d75fffb0-b553-4a6c-8d77-3780b40d0f91)
![5](https://github.com/user-attachments/assets/45124fd6-ee18-404c-9271-0042c2cc13a4)
![6](https://github.com/user-attachments/assets/b52e3ad5-5c44-46e8-aab0-e6173cb7f17e)
![7](https://github.com/user-attachments/assets/6991eddb-2194-4e08-9a49-c45ce1dd05be)
![10](https://github.com/user-attachments/assets/8b272785-ebb6-4a58-9b6a-72a91de0790e)
![8](https://github.com/user-attachments/assets/438d27d8-928d-4906-a466-cc91e0006905)
![9](https://github.com/user-attachments/assets/f45368ab-51b5-4522-b9e8-bd22328588a1)


# 🔧 개발 환경
* Java   </br>
* Spring Boot </br>
* MySQL </br>
* JPA </br>
* BootStrap </br>
* HTML/CSS </br>
* Gradle </br>
* Thymeleaf </br>

# 📝 다이어그램
 ![order-system drawio](https://github.com/user-attachments/assets/d60afcc3-f4d6-4b1f-bbed-f791a0240043)


   

# 📁 파일 구조
```
C:.
├─generated
│  └─com
│      └─clonecode
│          └─orderweb
│              └─domain             
├─java
│  └─com
│      └─clonecode
│          └─orderweb
│              │  OrderWebApplication.java
│              ├─configuration
│              ├─controller
│              ├─domain
│              ├─dto
│              ├─repository
│              ├─security
│              └─service              
└─resources
    ├─static      
    └─templates
```
       

# 🖱 실행 방법
**설치 준비**
* Java 17  

* Spring Boot 3.x.x  

* MySQL 8.0

**QueryDSL 설정**   </br>
* src/main/generated 디렉터리 생성   

**MySQL 데이터베이스 설정**  
+ 데이터베이스 이름: shopping_db

+ 사용자 이름/비밀번호 : webuser/webuser  

MySQL에 접속 후 아래의 명령어로 데이터베이스와 사용자 생성.
```
CREATE DATABASE shopping_db;  
CREATE USER 'webuser'@'localhost' IDENTIFIED BY 'webuser';  
GRANT ALL PRIVILEGES ON shopping_db.* TO 'webuser'@'localhost';
```

* MySQL을 도커로 실행할 경우
```
version: '3.8'
services:
  db:
    image: mysql:8.0
    container_name: mysql-container
    restart: always
    environment:
      MYSQL_DATABASE: shopping_db
      MYSQL_USER: webuser
      MYSQL_PASSWORD: webuser
      MYSQL_ROOT_PASSWORD: rootpassword
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```
  해당 yml 도커 컴포즈 파일 사용.    

**gradle 실행** </br>
아래의 명령어로 애플리케이션을 구동하고 http://localhost:8080 주소에 접속하여 확인.
```
./gradlew bootRun
```


### 상세 기록  

[상품 주문 클론 코딩] 1일차 https://www.notion.so/1-a5cbf2c8f9ab477684f95bd7bf95e15c?pvs=4

[상품 주문 클론 코딩] 2일차 https://www.notion.so/2-94f61d2a65a44f4fbe6ca5c2072eb585?pvs=21

[상품 주문 클론 코딩] 3일차 https://www.notion.so/3-3ffadfa8c14f42b294cc6b7ad598f706?pvs=21

[상품 주문 클론 코딩] 4일차 https://www.notion.so/4-07a1c3a191694ec8b1ec7fff97cb5eea?pvs=21

[상품 주문 클론 코딩] 5일차 https://www.notion.so/5-58b1ede9cdc74705a91109741da685b0?pvs=21

[상품 주문 클론 코딩] 6일차 https://www.notion.so/6-a6ae86c57ee04c268e269a39bcc9c821?pvs=21

[상품 주문 클론 코딩] 7일차 https://www.notion.so/7-e236dbf34c8e4573b1581ec2990b7979?pvs=21

[상품 주문 클론 코딩] 8일차 https://www.notion.so/8-db3e2bb09e8d429e8a02f6ca12bd8230?pvs=21

[상품 주문 클론 코딩] 9일차 https://www.notion.so/9-a53539b74f544f1b96b39edb42790618?pvs=21

[상품 주문 클론 코딩] 10일차 https://www.notion.so/10-a45c8bb8bbab4f329a198ac96c8022be?pvs=21

[상품 주문 클론 코딩] 11일차 https://www.notion.so/11-1c14732343974b25ba214d45ad258561?pvs=21

[상품 주문 클론 코딩] 12일차 https://www.notion.so/12-841554d65c83419595132af6e4e8865c?pvs=21

[상품 주문 클론 코딩] 13일차 https://www.notion.so/13-a697a015f07e4c0da90d34435dd5b381?pvs=21

[상품 주문 클론 코딩] 14일차 https://www.notion.so/14-387926e900624511acd8bb1cb51c02ea?pvs=21

[상품 주문 클론 코딩] 15일차 https://www.notion.so/15-b9300f475cac4886818efbd24d6d2bd0?pvs=21

[상품 주문 클론 코딩] 마무리 https://www.notion.so/188623a4cc89437ea1d197a5af4fd35c?pvs=21  











