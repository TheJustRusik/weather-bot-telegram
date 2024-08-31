FROM openjdk:17-jdk-slim

EXPOSE 8080

WORKDIR /app

RUN apt-get update && apt-get install -y wget curl

RUN curl -s https://api.github.com/repos/TheJustRusik/weather-bot-telegram/releases/latest \
    | grep fetcher.jar \
    | tail -n 1 \
    | cut -d : -f 2,3 \
    | tr -d \" \
    | wget -qi -

CMD ["java", "-jar", "fetcher.jar"]