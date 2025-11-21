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

# Aponta datasource (Render já injeta via env vars)
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}

# RabbitMQ (Render também injeta)
ENV SPRING_RABBITMQ_LISTENER_SIMPLE_AUTO_STARTUP=${SPRING_RABBITMQ_LISTENER_SIMPLE_AUTO_STARTUP}

# JWT
ENV JWT_SECRET=${JWT_SECRET}

# OpenAI key
ENV SPRING_AI_OPENAI_API_KEY=${SPRING_AI_OPENAI_API_KEY}

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]