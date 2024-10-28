FROM openjdk:17

WORKDIR /app

RUN mkdir /app/filebuket 

COPY target/instaclone-0.0.1-SNAPSHOT.jar /app/



CMD ["java","-jar","/app/instaclone-0.0.1-SNAPSHOT.jar"]