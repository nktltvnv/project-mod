FROM openjdk:24

WORKDIR /app

COPY build/libs/api-gateway-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
