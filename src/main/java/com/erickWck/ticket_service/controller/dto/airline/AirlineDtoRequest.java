package com.erickWck.ticket_service.controller.dto.airline;

import lombok.Builder;

@Builder(toBuilder = true)
public record AirlineDtoRequest(
        String name,
        String iacaoCode
) {
}
