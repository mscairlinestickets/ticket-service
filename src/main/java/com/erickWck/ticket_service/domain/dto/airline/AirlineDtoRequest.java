package com.erickWck.ticket_service.domain.dto.airline;

import lombok.Builder;

@Builder(toBuilder = true)
public record AirlineDtoRequest(
        String name,
        String iacaoCode
) {
}
