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

# Rodar com Maven
./gradlew bootRun
