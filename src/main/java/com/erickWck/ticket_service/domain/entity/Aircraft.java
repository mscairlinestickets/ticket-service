package com.erickWck.ticket_service.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder(toBuilder = true)
@Table(name = "aircraft")
public record Aircraft(

        @Id
        Long uuid,

        String model,

        String manufacturer,


        int seatCapacity
) {
}
