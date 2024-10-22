FROM openjdk:21

# 작업 디렉토리 설정
WORKDIR /bookNote

# 빌드된 JAR 파일을 컨테이너에 복사
COPY bookNote-0.0.1-SNAPSHOT.jar app.jar 

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
