FROM openjdk:21

WORKDIR /app

COPY target/order-registrar-service-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Run:
#   'docker build -t ivangorbunovv/registrar-service-order-processing-system-image .'
