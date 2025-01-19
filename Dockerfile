FROM maven:3.9.6-eclipse-temurin-21-alpine

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

EXPOSE 8080

#for debugging
#CMD ["tail", "-f", "/dev/null"]

CMD java -jar target/*.jar