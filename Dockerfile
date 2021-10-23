FROM openjdk:11-jre-slim
WORKDIR /algafood
ARG JAR_FILE
COPY target/${JAR_FILE} /algafood/api.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
EXPOSE 8080
CMD ["java", "-jar", "api.jar"]