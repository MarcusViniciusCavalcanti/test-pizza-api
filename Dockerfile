FROM maven:3.6.0-jdk-10 as appserver
WORKDIR /usr/src/api
COPY pom.xml .
RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
COPY . .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package

FROM openjdk:10-jdk
RUN adduser --disabled-password --home /home/application application
WORKDIR /backend
COPY --from=appserver /usr/src/api/target/api-0.0.1.jar .

ENTRYPOINT ["java", "-jar", "/backend/api-0.0.1.jar"]
CMD ["--spring.profiles.active=production"]