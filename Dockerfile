FROM maven:3.8.4-jdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Fase di runtime
FROM tomcat:9.0-jdk21
COPY --from=build /app/target/InOutFlow_Server.war /usr/local/tomcat/webapps/