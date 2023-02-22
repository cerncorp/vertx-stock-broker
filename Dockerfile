# ./mvnw clean package
# docker build -t example/vertx-stock-broker-starter .
# docker run -t -i -p 8888:8888 example/vertx-stock-broker-starter
# curl http://localhost:8888

FROM amazoncorretto:17

ENV FAT_JAR starter-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /usr/app

EXPOSE 8888

COPY target/$FAT_JAR $APP_HOME/

WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]
