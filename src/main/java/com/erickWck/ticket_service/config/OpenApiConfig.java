package com.erickWck.ticket_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catálogo de Passagens Aéreas")
                        .version("1.0")
                        .description("""
                        API desenvolvida como projeto pessoal por Erick Silva, voltada à gestão de companhias aéreas, aeronaves e voos — oferecendo um catálogo de passagens disponíveis para compra.

                        Este serviço compõe um dos microsserviços do ecossistema de passagens aéreas em desenvolvimento, servindo como vitrine funcional e técnica de boas práticas com Spring Boot, arquitetura limpa (Clean Architecture) e documentação via OpenAPI.
                        """)
                        .contact(new Contact()
                                .name("Erick Silva")
                                .email("erickk.nsilva100@gmail.com")
                                .url("https://github.com/erickknsilva"))
                );
    }




}
