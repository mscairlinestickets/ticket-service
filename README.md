[![Build Status](https://github.com/mscairlinestickets/ticket-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/mscairlinestickets/ticket-service/actions/workflows/commit-stage.yml)
[![License](https://img.shields.io/github/license/erickknsilva/ticket-service)](LICENSE)
# ✈️ Ticket Service

Sistema de **gestão de voos**, com funcionalidades para cadastrar e consultar voos, companhias aéreas e aeronaves.

---

---

## 🧪 Estratégia de Desenvolvimento

O projeto foi desenvolvido utilizando a abordagem **API First**, onde o contrato da API é definido antes da implementação.

Benefecios:
- Clareza nos endpoints expostos
- Desenvolvimento orientado por TDD
- Evolução desacoplada da interface REST

---

## 🚀 Tecnologias Utilizadas

- 🧠 **Linguagem & Plataforma**
  - Java 17
  - Spring Boot 3.5.x
  - Maven (gerenciador de dependências)

- 🧩 **Módulos & Frameworks Spring**
  - Spring Web – desenvolvimento de APIs RESTful
  - Spring Validation – validação de dados com Jakarta Bean Validation
  - Spring Data JPA – persistência com PostgreSQL
  - Spring Boot Actuator – métricas e health checks
  - Springdoc OpenAPI – geração automática de documentação Swagger

- 🧪 **Testes**
  - JUnit 5 – testes unitários
  - Mockito – mocks e spies
  - Testcontainers – testes de integração com PostgreSQL real
  - PIT Mutation Testing (PITest) – análise de qualidade dos testes

- 📦 **Observabilidade & Métricas**
  - Micrometer + Prometheus – coleta de métricas
  - OpenTelemetry (OTel Java Agent) – rastreabilidade distribuída (tracing)

- 🐳 **Infraestrutura & DevOps**
  - Docker – containerização da aplicação
  - GitHub Actions – CI/CD com build, testes e publicação de imagem
  - GHCR (GitHub Container Registry) – armazenamento da imagem gerada
  - Verifique a imagem gerada apos ser aprovada no pipeline [imagem ticket-service](https://github.com/mscairlinestickets?tab=packages) ou na [branch main](https://github.com/mscairlinestickets/ticket-service)
  
- 🛢️ **Banco de Dados**
  - PostgreSQL – banco de dados relacional
  - Flyway – versionamento e migração de schema


---
## 🛠️ Melhorias Futuras

O projeto está em evolução constante, com o objetivo de oferecer uma infraestrutura mais robusta, segura e escalável. As principais melhorias planejadas incluem:

- 🔐 **Integração com Spring Security**
  - Implementação de autenticação e autorização baseadas em JWT
  - Restrição de acesso por perfis de usuário
  - Segurança nos endpoints com configuração de filtros e `SecurityConfig`

- ☸️ **Orquestração com Kubernetes (K8s)**
  - Containerização completa dos serviços com Docker
  - Criação de manifests YAML para deploy em clusters Kubernetes
  - Configuração de health checks, readiness probes e autoescalabilidade
  - Deploy automatizado em ambientes como Minikube ou GKE (Google Kubernetes Engine)

Essas funcionalidades visam melhorar a **segurança**, a **observabilidade** e a **resiliência** do sistema em produção.


---

## 📚 Documentação da API

Acesse a documentação completa via Swagger:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧰 Como Rodar o Projeto

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/ticket-service.git

# Acessar o diretório
cd ticket-service

# Rodar com gradlew
./gradlew bootRun

```
```bash
✅ Executar Testes
./gradlew test 
#ou para mais informaçao 
./gradlew test --info

Os testes estão divididos em:
✅ Unitários: focados em DTOs, controllers,regras de negócio e integração
```

## 📦 Deploy

Este projeto utiliza **GitHub Actions** para integração contínua e publicação automatizada de imagens de container.

### 🔁 Pipeline CI/CD: `Commit Stage`

- A cada `push`, o pipeline executa:
  - **Build do projeto** com Gradle
  - **Testes unitários e de mutação (PITest)**
  - **Escaneamento de vulnerabilidades** no código e na imagem gerada
  - **Construção da imagem Docker** via `bootBuildImage`
  - **Publicação da imagem no GitHub Container Registry (GHCR)**

### 🐳 Imagem Docker

A imagem gerada é publicada com as tags:
- `${{ github.sha }}` → commit hash atual
- `latest` → para referência da versão mais recente

Voce pode encontra a imagem publicada neste link abaixo:  
[Container Registry](https://github.com/mscairlinestickets?tab=packages) ou na [branch main](https://github.com/mscairlinestickets/ticket-service)



---

### 📮 Endpoints da API

#### ✈️ Voos (`/api/flights`)

| Método   | Caminho                        | Descrição                                 | Status Esperado        | Corpo de Resposta                   |
|----------|-------------------------------|-------------------------------------------|--------------------------|-------------------------------------|
| `GET`    | `/api/flights`                | Retorna todos os voos                     | `200 OK`                 | Lista de `FlightDtoResponse`        |
| `GET`    | `/api/flights/{flightNumber}` | Busca um voo pelo número                  | `200 OK` / `404`         | Objeto `FlightDtoResponse` ou erro  |
| `POST`   | `/api/flights`                | Cria um novo voo                          | `201 Created`            | Objeto `FlightDtoResponse`          |
| `PUT`    | `/api/flights/{flightNumber}` | Atualiza um voo existente                 | `200 OK` / `404`         | Objeto `FlightDtoResponse`          |
| `DELETE` | `/api/flights/{flightNumber}` | Remove um voo                             | `204 No Content` / `404` | Nenhum corpo                        |

#### 🛫 Companhias Aéreas (`/api/airlines`)

| Método   | Caminho                         | Descrição                                 | Status Esperado          | Corpo de Resposta               |
|----------|----------------------------------|-------------------------------------------|---------------------------|----------------------------------|
| `GET`    | `/api/airlines`                | Retorna todas as companhias aéreas        | `200 OK`                  | Lista de `AirlineDtoResponse`   |
| `GET`    | `/api/airlines/{icaoCode}`     | Busca companhia aérea pelo código ICAO    | `200 OK` / `404`          | Objeto `AirlineDtoResponse`     |
| `POST`   | `/api/airlines`                | Cadastra uma nova companhia aérea         | `201 Created`             | Objeto `AirlineDtoResponse`     |
| `PUT`    | `/api/airlines/{icaoCode}`     | Atualiza uma companhia aérea existente    | `200 OK` / `404`          | Objeto `AirlineDtoResponse`     |
| `DELETE` | `/api/airlines/{icaoCode}`     | Remove companhia aérea pelo código ICAO   | `204 No Content` / `404`  | Nenhum corpo                    |

#### 🛩️ Aeronaves (`/api/aircraft`)

| Método   | Caminho                  | Descrição                                  | Status Esperado           | Corpo de Resposta               |
|----------|--------------------------|--------------------------------------------|----------------------------|----------------------------------|
| `GET`    | `/api/aircrafts`         | Retorna todas as aeronaves cadastradas     | `200 OK`                   | Lista de `AircraftDtoResponse`  |
| `GET`    | `/api/aircrafts/{model}` | Busca aeronave pelo modelo                 | `200 OK` / `404`           | Objeto `AircraftDtoResponse`    |
| `POST`   | `/api/aircrafts`         | Cadastra uma nova aeronave                 | `201 Created`              | Objeto `AircraftDtoResponse`    |
| `PUT`    | `/api/aircrafts/{model}` | Atualiza uma aeronave existente            | `200 OK` / `404`           | Objeto `AircraftDtoResponse`    |
| `DELETE` | `/api/aircrafts/{model}` | Remove aeronave pelo modelo                | `204 No Content` / `404`   | Nenhum corpo                    |

## 🗂️ Estrutura do Projeto

Abaixo está a organização dos principais pacotes e classes do microserviço `ticket-service`:

```bash
src/main/java/com/erickWck/ticket_service/
├── config # Configurações globais (ex: OpenAPI, auditoria)
│ ├── AuditorJpa.java
│ └── OpenApiConfig.java
├── controller # Camada de entrada (REST Controllers)
│ ├── AircraftController.java
│ ├── AirlineController.java
│ ├── FlightController.java
│ └── dto # DTOs usados pelos controllers
├── demo # Carga de dados para o perfil 'test'
│ └── TicketDemoDataLoader.java
├── domain # Domínio do sistema
│ ├── entity # Entidades JPA (modelo de dados)
│ ├── exception # Exceções personalizadas
│ ├── mapper # Conversões entre DTOs e entidades
│ ├── repository # Interfaces de repositórios JPA
│ └── service # Regras de negócio
└── TicketServiceApplication.java # Classe principal Spring Boot
```
## 👨‍💻 Autor

Desenvolvido por **Erick Silva**  
📎 [LinkedIn](https://www.linkedin.com/in/erick-silva-414098225/)  
💻 [GitHub](https://github.com/erickknsilva)
