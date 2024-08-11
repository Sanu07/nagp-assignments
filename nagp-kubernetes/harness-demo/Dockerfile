FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=target/harness-demo.jar
COPY ${JAR_FILE} harnes-demo.jar
ENTRYPOINT [ "java", "-jar", "/harnes-demo.jar" ]