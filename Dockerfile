#FROM openjdk:17-slim
#ARG JAR_FILE=/*.jar
#COPY ${JAR_FILE} manageaccount-0.0.1-SNAPSHOT.jar
#
##ENV WAIT_VERSION 2.9.0
#
#RUN echo "java -jar /manageaccount-0.0.1-SNAPSHOT.jar" > /run.sh

FROM openjdk:17-slim
COPY manageaccount-0.0.1-SNAPSHOT.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
CMD ["java","-Dlog4j2.formatMsgNoLookups=true","-Xms256m", "-Xmx512m", "-jar", "app.jar"]
