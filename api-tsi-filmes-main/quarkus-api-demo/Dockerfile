FROM eclipse-temurin:23-jdk-alpine

WORKDIR /app

COPY . ./

# Torne o mvnw executável
RUN chmod +x ./mvnw

# Build da aplicação
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

CMD ["sh", "-c", "java -jar target/quarkus-app/quarkus-run.jar"]