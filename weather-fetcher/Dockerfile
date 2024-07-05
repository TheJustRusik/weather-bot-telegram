FROM gradle:jdk17 as builder

WORKDIR /app

COPY ../weather-fetcher /app

RUN ./gradlew bootJar --no-daemon

FROM openjdk:17

EXPOSE 8080

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]