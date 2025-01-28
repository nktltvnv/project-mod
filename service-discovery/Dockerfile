FROM amd64/amazoncorretto:23

WORKDIR /app

COPY build/libs/service-discovery-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
