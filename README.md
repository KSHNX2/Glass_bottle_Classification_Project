# System Architecture
![병 판별 데이터 분석 및 웹 서비스 설계서_2](https://github.com/parkjunyeongg/bottle-project/assets/122770606/0ef1529b-0642-4467-9b75-23601e7767de)

# Spring Boot Class Diagram
![0602](https://github.com/parkjunyeongg/bottle-project/assets/122770606/9fe10031-42d4-4346-bd4d-9c71a4d88819)


Ubuntu 20.04 LTS 환경설정
=============

# DDNS
* bottle4.iptime.org
* bottle4.asuscomm.com

#VPN 
* PPTP
* Gateway 192.168.1.1
* Local ip 192.168.1.2

# PORT
* 22 : ssl, sftp
* 8080 : tomcat
* 80 : react
* 3306 : mariadb
* 55000 : flask

# Python
- 우분투에서 지원하는 파이썬 버전은 3.8이나 - yolov5를 위해 3.10.10으로 업그레이드 
- linux ver pytourch 설치
- pip3.10 설치
- ## Python 문제
  - python 내장 flask로는 정상동작 확인하였으나 외부 flask 사용시 에러발생
  - yolov5 설치에러 해결중 
- 외부 flask 서버를 배치함으로 해결함

# JAVA
- OpenJDK 1.8

# SpringBoot
- 2.7.11 버전으로 진행하였으나 포팅되지않아 2.7.7로 다운그레이드
- springsecurity 개발중
- ## REST_API
  - react 사진 다운로드 서비스 개발시 @RequestParm, Blob형식 사용

# HTTPS 사용을 위한 테스트 SSL 인증서 발급
## 환경 : window10, openssl 환경변수 설정

1. openssl 인증서 발급
 * private.key 이름의 개인키 발급
 - openssl genpkey -algorithm RSA -out private.key
 * public.key 이름의 공개키 발급 
 - rsa -in private.key -pubout -out public.key


2. CA인증을 대체하기 위해 자체 CA인증을 진행한다.
 - openssl genpkey -algorithm RSA -out rootCA.key
 - openssl req -new -key rootCA.key -out rootCA.csr
 - openssl x509 -req -days 365 -in rootCA.csr -signkey rootCA.key -out rootCA.crt
 
3. 스프링부트에서 사용하기 위해 p12 키스토어로 인코딩한다.
 - keytool -genkeypair -alias myapp -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650

4. 스프링부트 application.properties
 - server.port = 8080
 - server.ssl.key-store-type=PKCS12
 - server.ssl.key-store=c:/certs/keystore.p12 //경로 입력에 유의한다.
 - server.ssl.key-store-password=123456

