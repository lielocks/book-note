FROM gradle:8.3-jdk21 as builder
WORKDIR /build

# gradle 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . /build
RUN gradle build -x test --parallel

FROM openjdk:21-slim
WORKDIR /app

COPY --from=builder /build/build/libs/bookNote-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

USER nobody

ENTRYPOINT [ \
   "java", \
   "-jar", \
   "-Djava.security.egd=file:/dev/./urandom", \
   "-Dsun.net.inetaddr.ttl=0", \
   "bookNote-0.0.1-SNAPSHOT.jar" \
]
