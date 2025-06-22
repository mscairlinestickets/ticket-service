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
                        .title("Catalogo de Passagens aéreas.")
                        .version("1.0")
                        .description("Documentação dos endpoints da API de serviço de catalogo de passagens aéreas.")
                        .contact(new Contact().name("Erick Silva")
                                .email("erickk.nsilva100@gmail.com")
                                .url("https://github.com/erickknsilva"))
                );
    }


}
