ARG JAVA_VERSION=17
ARG MAVEN_VERSION=3.8.5

FROM maven:${MAVEN_VERSION}-eclipse-temurin-${JAVA_VERSION}-alpine AS MAVEN_BUILD
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip

FROM bellsoft/liberica-openjre-alpine-musl:${JAVA_VERSION}
WORKDIR /app
COPY --from=MAVEN_BUILD /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","./app.jar"]