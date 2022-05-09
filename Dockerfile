FROM openjdk:17-alpine
COPY build/libs/Java-Intern-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar Java-Intern-0.0.1-SNAPSHOT.jar