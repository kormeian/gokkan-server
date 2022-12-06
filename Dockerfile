FROM openjdk:11 AS builder
COPY build/libs/Gokkan-0.0.1-SNAPSHOT.jar bepo.jar
# 8080 컨테이너 포트 노출
EXPOSE 8080
# jar 파일 실행
ENTRYPOINT ["java","-jar","/bepo.jar"]