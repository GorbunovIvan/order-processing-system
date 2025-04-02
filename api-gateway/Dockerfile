FROM openjdk:21

WORKDIR /app

COPY target/api-gateway-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8765

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Run:
#   'docker build -t ivangorbunovv/api-gateway-order-processing-system-image .'
