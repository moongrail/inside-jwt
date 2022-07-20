FROM maven:3.6-jdk-11-slim AS build
COPY src /home/application/src
COPY pom.xml /home/application
USER root
RUN  mvn -DskipTests=true -f /home/application/pom.xml clean package
##chache

FROM openjdk:11-jre-slim
COPY --from=build /home/application/target/jwt.jar /usr/local/lib/jwt.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/usr/local/lib/jwt.jar"]