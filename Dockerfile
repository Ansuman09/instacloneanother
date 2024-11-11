FROM openjdk:17

WORKDIR /app

RUN mkdir /app/filebuket 
COPY target/instaclone-0.0.1-SNAPSHOT.jar /app/



CMD ["java","-jar","/app/instaclone-0.0.1-SNAPSHOT.jar"]

#Uncomment this and comment all above to generate a mysql container
#FROM mysql:8.4.0 

#ENV MYSQL_ROOT_PASSWORD qwerty11

#ADD ["schema.sql","/app/schema.sql"]

