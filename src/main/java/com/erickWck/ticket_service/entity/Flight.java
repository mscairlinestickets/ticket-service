package com.erickWck.ticket_service.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Flight {

    private Long id;

    private String flightNumber;

    private String origin;

    private String destination;

    private LocalDateTime departureDateTime;

    private int totalSeats;

    private int availableSeats;

    // Relação com a companhia aérea
//        @ManyToOne(optional = false)
//        @JoinColumn(name = "airline_id")
    private Airline airline;

    // Relação com o avião
//        @ManyToOne(optional = false)
//        @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;


}
