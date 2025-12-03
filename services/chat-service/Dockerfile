FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY target/chat-service-0.0.1-SNAPSHOT.jar chat-service.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "chat-service.jar"]

