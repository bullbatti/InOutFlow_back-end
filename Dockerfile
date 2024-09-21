RUN mvn clean package
FROM tomcat:9.0-jdk21
COPY target/InOutFlow_Server.war /usr/local/tomcat/webapps/