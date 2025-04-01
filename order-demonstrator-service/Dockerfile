FROM openjdk:21

WORKDIR /app

COPY target/order-demonstrator-service-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Run:
#   'docker build -t ivangorbunovv/demonstrator-service-order-processing-system-image .'
