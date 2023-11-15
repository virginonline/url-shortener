ARG JAVA_VERSION=17
ARG MAVEN_VERSION=3.8.5


FROM maven:${MAVEN_VERSION}-openjdk-${JAVA_VERSION} AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM bellsoft/liberica-openjdk-alpine-musl:${JAVA_VERSION}
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","./app.jar"]