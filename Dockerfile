FROM clojure as buildStage

LABEL stage="builder"

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN apt-get -y update

RUN lein deps

RUN lein uberjar

FROM amazoncorretto:11-alpine

WORKDIR /app

COPY --from=buildStage /usr/src/app/target/stethoscope-0.1.0-SNAPSHOT-standalone.jar  /app/stethoscope.jar

CMD ["java", "-jar", "stethoscope.jar"]