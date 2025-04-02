FROM openjdk:21

WORKDIR /app

COPY target/eureka-server-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Run:
#   'docker build -t ivangorbunovv/eureka-server-order-processing-system-image .'
