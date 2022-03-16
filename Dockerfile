FROM openjdk:8-jre-alpine3.9

#RUN apt install default-jdk

ADD ./target/rewards-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/

EXPOSE 9002
VOLUME /app/logs

CMD java -jar rewards-0.0.1-SNAPSHOT.jar
