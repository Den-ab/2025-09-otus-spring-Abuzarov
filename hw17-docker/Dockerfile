FROM amazoncorretto:21.0.6-alpine

WORKDIR /app

COPY target/hw17-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-jar", "app.jar"]
