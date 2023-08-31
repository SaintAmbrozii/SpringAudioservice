FROM openjdk:17

EXPOSE 8080

ADD target/Audioservice-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Dspring.profiles.aktive","-jar","/app/app.jar"]