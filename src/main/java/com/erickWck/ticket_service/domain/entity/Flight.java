package com.erickWck.ticket_service.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Table(name = "flight")
public class Flight {

    @Id
    private Long id;

    private String flightNumber;

    private String origin;

    private String destination;

    private LocalDateTime departureDateTime;

    private int totalSeats;

    private int availableSeats;

    private BigDecimal price;
    // Relação com a companhia aérea
//        @ManyToOne(optional = false)
//        @JoinColumn(name = "airline_id")
    private Airline airline;

    // Relação com o avião
//        @ManyToOne(optional = false)
//        @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;


}
