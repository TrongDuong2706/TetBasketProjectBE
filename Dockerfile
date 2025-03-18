FROM maven:3.9.9-eclipse-temurin-21-alpine as build
WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

## run stage ##
FROM amazoncorretto:21.0.5-alpine3.20
WORKDIR /run
COPY --from=build /app/target/identity-service-0.0.1-SNAPSHOT.jar /run/identity-service-0.0.1-SNAPSHOT.jar

RUN adduser -D trongduong

RUN chown -R trongduong:trongduong /run

USER trongduong
EXPOSE 8080
ENTRYPOINT java -jar /run/identity-service-0.0.1-SNAPSHOT.jar