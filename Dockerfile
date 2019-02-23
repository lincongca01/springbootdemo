# Our base image that contains OpenJDK
FROM openjdk:8-jre-alpine 
#FROM openjdk:8-jdk-alpine 
#RUN mkdir /app
#COPY mvnw pom.xml /app/
#COPY src /app/src
#COPY .mvn /app/.mvn
#WORKDIR /app
## Build jar file and add the fatjar in the image
#RUN ./mvnw package && cp ./target/demo-0.0.1-SNAPSHOT.jar / 
#WORKDIR /
#RUN rm -r /app /root/.m2
# Add the fatjar in the image
COPY target/demo-0.0.1-SNAPSHOT.jar / 
# Default command
CMD java -jar /demo-0.0.1-SNAPSHOT.jar