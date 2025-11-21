# Usa a imagem oficial do Java 17
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copia o Maven wrapper e o pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Baixa dependências (cache eficiente)
RUN ./mvnw dependency:go-offline

# Copia o restante do código
COPY src src

# Build do projeto
RUN ./mvnw -DskipTests package

# ----------------------------------------
# Imagem final
# ----------------------------------------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o .jar gerado
COPY --from=builder /app/target/*.jar app.jar

# Porta padrão do Render
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]