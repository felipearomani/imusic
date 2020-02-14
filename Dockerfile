FROM openjdk:11-jdk
VOLUME /tmp
COPY target/*.jar imusic-service.jar

ENV REDIS_PORT=""
ENV REDIS_HOST=""
ENV SPOTIFY_CLIENT_ID=""
ENV SPOTIFY_CLIENT_SECRET=""
ENV OPEN_WEATHER_APP_ID=""


ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "imusic-service.jar"]