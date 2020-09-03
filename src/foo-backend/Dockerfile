FROM maven:3.6.3-openjdk-8-slim

RUN mkdir /usr/src/app
WORKDIR /usr/src/app

ADD pom.xml /usr/src/app/
RUN mvn dependency:go-offline

ADD . /usr/src/app
ADD configs /usr/src/app/target

RUN mvn package

WORKDIR /usr/src/app/target

EXPOSE 8055
EXPOSE 9009

ENTRYPOINT java -jar -Dvmr-config-file=./conf/development.yml -Dvmr-redis-config-file=./conf/redis.yml -Dlog4j2.configurationFile=./log4j2.xml foo-backend-1.0-SNAPSHOT.jar

java -Dservice.conf=./conf/development.yaml -Dlog4j2.configurationFile=./conf/log4j2.xml -Dredis.conf=./conf/redis.yaml -cp foo-backend-1.0-SNAPSHOT.jar vn.zalopay.phucvt.fooapp.Runner
