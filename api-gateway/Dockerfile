FROM amd64/amazoncorretto:23

WORKDIR /app

COPY build/libs/api-gateway-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
