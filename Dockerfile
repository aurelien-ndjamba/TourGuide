FROM openjdk:8-jre-alpine3.9

#RUN apt install default-jdk

ADD ./target/gpsUtil-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/

EXPOSE 9001
VOLUME /app/logs

CMD java -jar gpsUtil-0.0.1-SNAPSHOT.jar
