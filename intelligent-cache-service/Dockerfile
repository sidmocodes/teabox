FROM openjdk:17
COPY . /app
WORKDIR /app
RUN ./mvnw package
CMD ["java", "-jar", "target/app.jar"]