# 🌱 EcoWork — Plataforma de Sustentabilidade Corporativa
API Java Spring Boot • Global Solution • FIAP 2025

---

## Visão Geral do Projeto

A **EcoWork** é uma plataforma projetada para ajudar empresas a medir, acompanhar e incentivar ações de sustentabilidade no ambiente de trabalho.

A aplicação permite que empresas e colaboradores:

- registrem consumos (energia, papel, transporte, etc.);
- transformem economia em **pontos verdes**;
- acompanhem **metas de sustentabilidade**;
- visualizem **rankings** de colaboradores;
- utilizem **IA generativa** para obter dicas e insights de redução de impacto ambiental.

Este repositório contém **a API REST Java Spring Boot**, responsável por:
Autenticação e autorização com **JWT** (Spring Security)CRUD completo das entidades principais Gestão de empresas, usuários, metas, sensores e registros de consumo Cálculo automático de pontuação (gamificação verde)Ranking global e por empresa Mensageria assíncrona com **RabbitMQ**Integração com **Spring AI + OpenAI** para geração de sugestões sustentáveis Deploy em nuvem via **Render** (Docker + PostgreSQL)Internacionalização (**pt-BR** / **en-US**)Cache com **Caffeine** para melhorar performance Tratamento global de erros e respostas padronizadas

 Futuro: esta API será consumida por um **app mobile** gamificado (React Native / outra stack escolhida).

---

# Problema / Tema da GS

> **EcoWork — Trabalho Sustentável e Consciente**  
> Propósito: ajudar empresas a medir o impacto ambiental das suas operações e incentivar funcionários a reduzir sua pegada de carbono.

- ODSs abordados:
    - **ODS 12** – Consumo e produção responsáveis
    - **ODS 13** – Ação contra a mudança global do clima
    - **ODS 8** – Trabalho decente e crescimento econômico

A API fornece os serviços centrais da solução:

- controle de empresas, usuários e seus papéis (admin / funcionário);
- cadastro de sensores físicos (IoT) associados à empresa;
- registro de consumos vindo desses sensores ou lançados manualmente;
- definição de **metas sustentáveis** (ex.: reduzir X kWh, papel ou transporte);
- cálculo de **pontos verdes** baseados nas ações de economia;
- geração de **relatórios e ranking** para engajar os colaboradores;
- apoio com IA generativa para recomendações de boas práticas.

---

# Tecnologias Utilizadas

| Componente                         | Tecnologia / Versão                         |
|-----------------------------------|---------------------------------------------|
| Linguagem                         | Java 17                                     |
| Framework                         | Spring Boot 3.4.x                           |
| ORM                               | Spring Data JPA                             |
| Banco de Dados                    | PostgreSQL (Render)                         |
| Migrações                         | Flyway (scripts em `db/migration`)         |
| Segurança                         | Spring Security + JWT                       |
| Validação                         | Bean Validation (`jakarta.validation`)      |
| Cache                             | Spring Cache + Caffeine                     |
| Mensageria                        | Spring AMQP + RabbitMQ                      |
| IA Generativa                     | Spring AI + OpenAI                          |
| Internacionalização (i18n)        | `messages.properties` / `messages_en.properties` |
| Build                             | Maven                                       |
| Deploy                            | Render Web Service (Dockerfile)             |

---

# Estrutura do Projeto

```text
ecowork/
├── src/
│   ├── main/
│   │   ├── java/com/ecowork
│   │   │   ├── config/                 # Configurações transversais (cache, i18n, RabbitMQ...)
│   │   │   │   ├── CacheConfig.java
│   │   │   │   ├── I18nConfig.java
│   │   │   │   └── RabbitConfig.java
│   │   │   ├── controller/api/         # Controladores REST
│   │   │   │   ├── AiController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── EmpresaController.java
│   │   │   │   ├── MetaController.java
│   │   │   │   ├── PontuacaoController.java
│   │   │   │   ├── RankingController.java
│   │   │   │   ├── RegistroConsumoController.java
│   │   │   │   ├── SensorController.java
│   │   │   │   └── UsuarioController.java
│   │   │   ├── dto/                    # DTOs de requisição e resposta
│   │   │   │   ├── ai/
│   │   │   │   ├── auth/
│   │   │   │   ├── empresa/
│   │   │   │   ├── meta/
│   │   │   │   ├── pontos/
│   │   │   │   ├── ranking/
│   │   │   │   ├── registro/
│   │   │   │   ├── sensor/
│   │   │   │   └── usuario/
│   │   │   ├── exception/              # Tratamento global de erros
│   │   │   │   ├── ApiError.java
│   │   │   │   ├── ApiFieldError.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── NotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── mapper/                 # Conversores Entity <-> DTO
│   │   │   ├── messaging/              # Eventos e integração RabbitMQ
│   │   │   ├── models/                 # Entidades JPA + Enums
│   │   │   ├── repository/             # Repositórios Spring Data JPA
│   │   │   ├── security/               # JWT, filtros, configuração de segurança
│   │   │   ├── service/                # Regras de negócio / casos de uso
│   │   │   └── EcoWorkApplication.java # Classe principal (Spring Boot)
│   │   └── resources/
│   │       ├── db/migration/           # Scripts Flyway V1, V2, V3...
│   │       ├── messages.properties     # i18n PT-BR
│   │       ├── messages_en.properties  # i18n EN
│   │       └── application.yaml        # Configurações da aplicação
│   └── test/java/com/ecowork
│       └── EcoWorkApplicationTests.java
├── Dockerfile
├── pom.xml
└── HELP.md (sugestões de uso do Spring Boot)
```

---

## Principais Domínios da Aplicação
### Empresa
- Cadastro de empresas clientes da plataforma EcoWork.

- Campos típicos: ```nome```, ```cnpj```, ```endereco```, etc.

- Relações:

    - 1:N com **Usuário (funcionários e administradores)

    - 1:N com **Sensor**

    - 1:N com **MetaSustentavel**

### Usuário
- Representa colaborador ou administrador da empresa.

- Enum ```RoleUsuario```:

    - ```EMPLOYEE``` — funcionário

    - ```COMPANY_ADMIN``` — admin da empresa

- Campos relevantes:

    - ```nome```, ```email``` (único), ```senha``` (hash)

    - ```pontosTotais``` (gamificação)

- Segurança:

    - usado pelo ```UserDetailsService``` para autenticação JWT.

### Sensor
- Representa um sensor (ou fonte de dados) associado à empresa.

- Exemplo: medidor de energia, controle de transporte, etc.

- Campos:

    - ```tipoSensor``` (ENERGIA, PAPEL, TRANSPORTE)

    - ```localizacao```

    - ```empresaId```

- Usado pelos registros de consumo.

### Meta Sustentável
- Define um objetivo de redução / controle de consumo.

- Campos:

    - ```tipo``` (mesma enum de consumo)

    - ```empresaId```

    - ```valorAlvo```

    - ```dataInicio```, ```dataFim```

    - ```empresaId```

    - ```status``` (enum StatusMeta: ATIVA, CONCLUIDA, EXPIRADA)

- Avaliada a partir dos registros e pontuação.

### Registro de Consumo
- Registra ações de consumo/economia ligadas a um usuário e sensor.

- Campos:

    - ```tipo```, ```valor```, ```usuarioId```, ```metaId```, ```sensorId```, ```dataRegistro```

- Dispara lógica de:

    - cálculo de pontos;

    - atualização de ```pontosTotais``` do usuário;

    - geração de evento em fila RabbitMQ (```ConsumoEvent```).

### Pontuação e Ranking
- Regra atual:

    - ```ENERGIA``` → ```valor * 10``` pontos

    - (demais tipos podem ser estendidos)

- O serviço de pontuação alimenta:

    - ```PontuacaoRepository```

    - ```RankingService``` (ranking global e por empresa).

---

## Autenticação & Autorização (Spring Security + JWT)
- Endpoints públicos (exemplos):

    - ```POST /auth/login```

- Endpoints protegidos:

    - ```/api/**``` (empresas, usuários, metas, sensores, consumos, ranking, IA, etc.)

- Pipeline:

    1. Usuário faz login com email + senha;

    2. ```AuthService``` autentica credenciais via ```AuthenticationManager```;

    3. ```JwtService``` gera token JWT (contendo ```sub``` = email, ```roles```, etc.);

    4. O token é enviado ao cliente;

    5. Nas próximas requisições, cliente envia ```Authorization: Bearer <token>```;

    6. ```JwtAuthenticationFilter``` valida token e popula o ```SecurityContext```.

- Restrições de acesso:

    - Admin de empresa (```COMPANY_ADMIN```) pode:

      - cadastrar sensores, metas, usuários, consultar ranking da própria empresa, etc.

    - Funcionário (```EMPLOYEE```) pode:

      - registrar consumo, consultar seus pontos, ranking, etc.

---

## Validação, Erros & Respostas
- Bean Validation aplicado nos DTOs (```@NotNull```, ```@NotBlank```, ```@Email```, etc.);

- ```GlobalExceptionHandler``` centraliza:

    - ```MethodArgumentNotValidException``` → retorna erros de campo (```ApiFieldError```);

    - ```BusinessException``` → regras de negócio violadas;

    - ```NotFoundException``` → recursos inexistentes;

    - Demais exceções → erro 500 padronizado.

Exemplo de resposta de erro:

```
{
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Dados inválidos",
  "path": "/api/usuarios",
  "timestamp": "2025-11-18T01:23:45Z",
  "fields": [
    { "field": "email", "message": "must be a well-formed email address" }
  ]
}
```

---

## Internacionalização (i18n)
- Arquivos:

    - ```messages.properties``` (pt-BR)

    - ```messages_en.properties``` (en-US)

- Configuração:

    - ```I18nConfig``` ajusta ```LocaleResolver``` e o suporte a múltiplos idiomas.

    - ```spring.mvc.locale=pt_BR``` e ```locale-resolver=accept-header```.

- Mensagens de validação e de negócio podem ser lidas via ```MessageSource```.

---

## Cache com Caffeine
- Habilitado via ```CacheConfig``` / ```@EnableCaching```.

- Implementado em serviços de leitura mais pesada, como:

    - Ranking global / por empresa;

    - Consultas agregadas de pontos.

- Provider:

    - ```spring.cache.type=caffeine```

    - Policy: ```maximumSize=1000, expireAfterWrite=10m```.

---

## Mensageria com RabbitMQ
- Configuração em ```RabbitConfig```:

    - Exchange, fila e routing key para eventos de consumo (```ConsumoEvent```).

- ```ConsumoProducer```:

    - Envia mensagem quando um novo registro de consumo é criado.

- ```ConsumoConsumer```:

    - Consome a mensagem de forma assíncrona (simulação de auditoria/log externo).

- Possibilidades:

    - replicar eventos para outros serviços;

    - fazer análises offline;

    - registrar histórico de auditoria (```LogEvento```, por exemplo).

---

## Spring AI (OpenAI)
- Serviço: ``````EcoAiService`````` + ``````AiController``````.

- Uso:

    - A API recebe uma pergunta sobre economia de energia / sustentabilidade;

    - O serviço monta um prompt explicando o contexto EcoWork;

    - Spring AI chama o modelo (``````gpt-4o-mini``````, configurado no ``````application.yaml``````);

    - A resposta é retornada em texto para o cliente.

Exemplo de endpoint:

```
POST /api/ai/sugestoes
Content-Type: application/json

{
  "pergunta": "Como posso reduzir o consumo de energia no escritório?"
}
```

Resposta (exemplo resumido):

```
{
  "resposta": "Você pode começar substituindo lâmpadas por LED, ajustando o uso de ar-condicionado..."
}
```

---

## Deploy em Nuvem (Render + Docker)

### Dockerfile (resumo)
- Usa imagem base OpenJDK 17;

- Faz build via Maven (```./mvnw package -DskipTests```);

- Copia o ```app.jar``` para a imagem final;

- Expõe a porta padrão (8080);

- ```ENTRYPOINT ["java", "-jar", "/app/app.jar"]```.

### Variáveis de Ambiente (Render)
No serviço web da API foram configuradas variáveis, por exemplo:

- ```SPRING_DATASOURCE_URL``` — jdbc:postgresql://<host>:5432/ecowork_db

- ```SPRING_DATASOURCE_USERNAME``` — ecowork_db_user

- ```SPRING_DATASOURCE_PASSWORD``` — <senha>

- ```SPRING_MESSAGES_BASENAME``` — messages

- ```SPRING_PROFILES_ACTIVE``` — prod

- ```SPRING_RABBITMQ_LISTENER_SIMPLE_AUTO_STARTUP``` — false

- ```SPRING_AI_OPENAI_API_KEY``` — <sua_openai_key>

- ```JWT_SECRET``` — segredo do token JWT

- ```DATABASE_URL``` — URL externa do PostgreSQL (auxiliar/documentação)

O ```application.yaml``` lê essas variáveis via ${...} para diferenciar ambiente local x produção.

---

## Como Rodar o Projeto Localmente
1. Pré-requisitos
    - Java 17 instalado

    - Maven 3.x (ou usar o ```mvnw```)

    - PostgreSQL rodando localmente

    - RabbitMQ (opcional para testes básicos; recomendado para testar mensageria)

    - Variável de ambiente com sua chave OpenAI (para os endpoints de IA)

2. Clonar repositório
```
git clone https://github.com/murilors27/ecowork-api-java.git
cd ecowork-api-java
```

3. Configurar Banco Local
Crie um banco:

```
CREATE DATABASE ecowork;
```

Ajuste ```application.yaml``` (perfil local) se necessário:

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecowork
    username: postgres
    password: admin123
```

4. Rodar as migrações Flyway e subir a API

```
./mvnw spring-boot:run
```

Ou:

```
mvn spring-boot:run
```
- Flyway executará os scripts em ```db/migration```:

    - ```V1__create_tables.sql```

    - ```V2__insert_empresa_admin.sql```

    - ```V3__seed_base_data.sql```

    - ```V4__insert_sensores_basicos.sql```

    - ```V5__create_log_evento.sql```

- A aplicação sobe por padrão em http://localhost:8080.

---

## Roteiro de Testes (Manual / Postman / Insomnia)
Sugestão de testes para demonstração (seguindo a ordem lógica da solução).

  - Todos os endpoints abaixo (exceto login) devem ser chamados com
Authorization: Bearer <token>.

### 1️⃣ Criar Empresa

#### POST /api/empresa

```
{
  "nome": "EcoTech Ltda",
  "cnpj": "12345678000199",
  "endereco": "Av. Sustentável, 100"
}
```
- Esperado:

  - HTTP ```201 Created```

  - corpo com id da empresa.

- Guarde: empresaId.

###2️⃣ Criar Administrador da Empresa

Se não vier pré-criado pelos scripts, criar:

#### POST /api/usuarios

```
{
  "nome": "Admin Eco",
  "email": "admin@ecowork.com",
  "senha": "admin123",
  "empresaId": 1
}
```

- Usuário inicialmente com role ```EMPLOYEE``` (dependendo do mapper).

- Ajustar manualmente no banco para ```COMPANY_ADMIN``` (ou usar script).

### 3️⃣ Login

#### POST /auth/login

```
{
  "email": "admin@ecowork.com",
  "senha": "admin123"
}
```

- Esperado:

  - HTTP ```200 OK```

  - corpo:

```
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

- Guarde token e use em todas as próximas requisições.

### 4️⃣ Criar Sensor


#### POST /api/sensores
- Authorization: Bearer <token>

```
{
  "tipoSensor": "ENERGIA",
  "localizacao": "Andar 3 - Sala 18",
  "empresaId": 1
}
```

- Esperado: ```201 Created``` + ```sensorId```.

### 5️⃣ Criar Meta Sustentável

#### POST /api/metas

- Authorization: Bearer <token>

```
{
  "tipo": "ENERGIA",
  "valorAlvo": 100,
  "dataInicio": "2025-01-01",
  "dataFim": "2025-12-31",
  "empresaId": 1
}
```

- Esperado:

  - ``````metaId``````

  - ``````status``````: ``````"ATIVA"``````.

### 6️⃣ Criar Usuário Funcionário

#### POST /api/usuarios
- Authorization: Bearer <token>

```
{
  "nome": "Murilo Funcionário",
  "email": "murilo@eco.com",
  "senha": "murilo123",
  "empresaId": 1
}
```

- Esperado:

  - ```usuarioId```

  - ```pontosTotais```: 0.

### 7️⃣ Criar Registro de Consumo (gera pontos)

#### POST /api/consumos
- Authorization: Bearer <token>

```
{
  "tipo": "ENERGIA",
  "valor": 7,
  "usuarioId": 2,
  "metaId": 1,
  "sensorId": 1
}
```

- Regra exemplo: ```ENERGIA → valor * 10```

  - 7 → 70 pontos.

- Esperado:

  - ```201 Created```

  - Dados do registro, incluindo dataRegistro.

### 8️⃣ Ver Pontos do Usuário

#### GET /api/usuarios/2
- Authorization: Bearer <token>

- Esperado:
  - ```pontosTotais = 70```.

### 9️⃣ Ver Ranking Global

#### GET /api/ranking/global
Authorization: Bearer <token>
- Esperado (exemplo):

```
[
  {
    "usuarioId": 2,
    "nome": "Murilo Funcionário",
    "empresa": "EcoTech Ltda",
    "pontosTotais": 70
  }
]
```

### 🔟 Listagens Importantes
- Sensores por empresa:


#### GET /api/sensores/empresa/1
Authorization: Bearer <token>

- Metas por empresa:


#### GET /api/metas/empresa/1
Authorization: Bearer <token>

- Registros por usuário:

#### GET /api/consumos/usuario/2
Authorization: Bearer <token>

- Ranking por empresa:

#### GET /api/ranking/empresa/1

Authorization: Bearer <token>

### Teste do Endpoint de IA

#### POST /api/ai/sugestoes

Authorization: Bearer <token>

```
{
  "pergunta": "Como reduzir o consumo de papel no escritório?"
}
```

- Esperado:

  - Dica textual gerada pela IA com base no contexto EcoWork.

---

## Deploy e Acesso

A aplicação está hospedada no **Render**, com acesso público.  
O app mobile consome diretamente o endpoint do serviço online.

**URL do Deploy:** [[deploy](https://ecowork-api.onrender.com)]

---

## Apresentação e Demonstração Técnica
 

- 🔗 *Link para o Pitch:* [[pitch](https://youtu.be/qS40lLEHgVg)]
- 🔗 *Link para a Demo:* [[demo](https://youtu.be/tCC9frSl5_w)]

---

## Licença
Projeto desenvolvido para fins acadêmicos (FIAP — Global Solution).
Uso e modificação livres para estudo. Para uso comercial, recomenda-se revisão e adequações.

---

## Equipe de Desenvolvimento

| Nome                                | RM       | GitHub                                |
|-------------------------------------|----------|----------------------------------------|
| **Murilo Ribeiro Santos**           | RM555109 | [@murilors27](https://github.com/murilors27) |
| **Thiago Garcia Tonato**            | RM99404  | [@thiago-tonato](https://github.com/thiago-tonato) |
| **Ian Madeira Gonçalves da Silva**  | RM555502 | [@IanMadeira](https://github.com/IanMadeira) |

**Curso:** Análise e Desenvolvimento de Sistemas  
**Instituição:** FIAP — Faculdade de Informática e Administração Paulista  
**Ano:** 2025
