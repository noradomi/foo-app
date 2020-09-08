FROM maven:3.6.3-openjdk-8-slim

RUN mkdir /usr/src/app

ADD /foo-backend/pom.xml /usr/src/app/
WORKDIR /usr/src/app
RUN mvn dependency:go-offline

ADD /foo-backend/ /usr/src/app

RUN mvn clean
# ADD foo-run/foo-backend/foo-backend-1.0-SNAPSHOT.jar /usr/src/app
ADD foo-run/foo-backend/conf /usr/src/app/target/conf
RUN mvn package


WORKDIR /usr/src/app/target

EXPOSE 8055
EXPOSE 9009

ENTRYPOINT java -Dservice.conf=./conf/development.yaml -Dlog4j2.configurationFile=./conf/log4j2.xml -Dredis.conf=./conf/redis.yaml -cp foo-backend-1.0-SNAPSHOT.jar vn.zalopay.phucvt.fooapp.Runner
