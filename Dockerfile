FROM openjdk:8-jre-alpine3.9

#RUN apt install default-jdk

ADD ./target/tourguide-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/

EXPOSE 8080
VOLUME /app/logs

CMD java -jar tourguide-0.0.1-SNAPSHOT.jar
