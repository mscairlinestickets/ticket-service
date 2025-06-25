package com.erickWck.ticket_service.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder(toBuilder = true)
@Table(name = "airlines")
public record Airline(

        @Id
        Long id,
        String name,
        String icaoCode
) {

}
