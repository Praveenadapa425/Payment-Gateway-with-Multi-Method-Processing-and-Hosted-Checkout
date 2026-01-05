FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY . .

RUN ./mvnw package -DskipTests

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "target/payment-gateway-0.0.1-SNAPSHOT.jar"]