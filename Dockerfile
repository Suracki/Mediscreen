FROM eclipse-temurin:11
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service
RUN mvn package -Dmaven.test.skip
CMD ["java","-jar","target/mediscreen-0.1.0-SNAPSHOT.jar"]