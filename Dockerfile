FROM openjdk:17-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} manageaccount-0.0.1-SNAPSHOT.jar

#ENV WAIT_VERSION 2.9.0

RUN echo "java -jar /manageaccount-0.0.1-SNAPSHOT.jar" > /run.sh


