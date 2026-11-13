FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 10001 appuser
COPY --from=build /build/target/sistema-biblioteca-2.0.0.jar app.jar
COPY --from=build /build/target/lib ./lib
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
