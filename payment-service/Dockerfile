FROM openjdk:17-jdk-alpine3.14

ARG JAR_FILE=payment-service-0.0.1-SNAPSHOT.jar

RUN mkdir -p /apps
COPY ./target/${JAR_FILE} /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh
RUN chmod +x /apps/entrypoint.sh

EXPOSE 8081
CMD ["/apps/entrypoint.sh"]
