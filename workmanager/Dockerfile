FROM eclipse-temurin:17-jre

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENV TZ=Asia/Seoul
ENV LANG=C.UTF-8

ENTRYPOINT ["java", "-jar", "app.jar"]