ARG JAVA_VERSION=17
ARG MAVEN_VERSION=3.8.5

FROM maven:${MAVEN_VERSION}-openjdk-${JAVA_VERSION} AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM bellsoft/liberica-openjdk-alpine:${JAVA_VERSION}
COPY --from=build /target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","./app.jar"]