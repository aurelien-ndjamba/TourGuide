FROM openjdk:8-jre-alpine3.9

#RUN apt install default-jdk

ADD ./target/tripPricer-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/

EXPOSE 9003
VOLUME /app/logs

CMD java -jar tripPricer-0.0.1-SNAPSHOT.jar
