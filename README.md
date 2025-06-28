# ✈️ Ticket Service

Sistema de **gestão de voos**, com funcionalidades para cadastrar e consultar voos, companhias aéreas e aeronaves.

---

## 📌 Funcionalidades

- Cadastro e consulta de voos (`Flight`)
- Gestão de companhias aéreas (`Airline`)
- Cadastro de aeronaves (`Aircraft`)
- Validações de integridade nos DTOs
- Documentação gerada via **OpenAPI / Swagger**

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Validation
- JUnit 5 + Mockito
- Maven
- Swagger UI
- PostgreSQL
- (Inclua aqui: PostgreSQL, Docker, etc. conforme for usando)

---

## 🧪 Estratégia de Desenvolvimento

O projeto foi desenvolvido utilizando a abordagem **API First**, onde o contrato da API é definido antes da implementação. Isso garante:

- Clareza nos endpoints expostos
- Testes unitários orientados por TDD
- Evolução desacoplada da interface REST

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
./gradlew test ou ./gradlew test --info
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

Voce pode encontra a imagem publicada neste link ou na branch main: 
https://github.com/mscairlinestickets?tab=packages


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

| Método   | Caminho                         | Descrição                                  | Status Esperado           | Corpo de Resposta               |
|----------|----------------------------------|--------------------------------------------|----------------------------|----------------------------------|
| `GET`    | `/api/aircraft`                | Retorna todas as aeronaves cadastradas     | `200 OK`                   | Lista de `AircraftDtoResponse`  |
| `GET`    | `/api/aircraft/{model}`        | Busca aeronave pelo modelo                 | `200 OK` / `404`           | Objeto `AircraftDtoResponse`    |
| `POST`   | `/api/aircraft`                | Cadastra uma nova aeronave                 | `201 Created`              | Objeto `AircraftDtoResponse`    |
| `PUT`    | `/api/aircraft/{model}`        | Atualiza uma aeronave existente            | `200 OK` / `404`           | Objeto `AircraftDtoResponse`    |
| `DELETE` | `/api/aircraft/{model}`        | Remove aeronave pelo modelo                | `204 No Content` / `404`   | Nenhum corpo                    |


👨‍💻 Autor
Erick Silva – [LinkedIn](https://www.linkedin.com/in/erick-silva-414098225/) e [GitHub](https://github.com/erickknsilva)





