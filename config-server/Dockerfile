FROM openjdk:21

WORKDIR /app

COPY target/config-server-1.0-SNAPSHOT.jar ./app.jar
#COPY config-repo/ config-repo

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=local-docker"]

# Run:
#   'docker build -t ivangorbunovv/config-server-order-processing-system-image .'
