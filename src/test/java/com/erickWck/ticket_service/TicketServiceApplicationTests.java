package com.erickWck.ticket_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersServiceConfiguration.class)
@SpringBootTest
class TicketServiceApplicationTests {


    @Test
    void contextLoads() {
    }
}
