package com.erickWck.ticket_service.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;

    private String origin;

    private String destination;

    private LocalDateTime departureDateTime;

    private int totalSeats;

    private int availableSeats;

    private BigDecimal price;


    @ManyToOne(optional = false)
    @JoinColumn(name = "airline", referencedColumnName = "id")
    private Airline airline;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aircraft", referencedColumnName = "uuid")
    private Aircraft aircraft;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private int version;

    public Flight(String flightNumber, String origin, String destination, LocalDateTime departureDateTime, int totalSeats, int availableSeats, BigDecimal price, Airline airline, Aircraft aircraft, Instant createdDate, Instant lastModifiedDate, int version) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
        this.airline = airline;
        this.aircraft = aircraft;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
    }


    public Flight(Long id, String flightNumber, String origin, String destination, LocalDateTime departureDateTime, int totalSeats, int availableSeats, BigDecimal price, Airline airline, Aircraft aircraft, Instant createdDate, Instant lastModifiedDate, int version) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
        this.airline = airline;
        this.aircraft = aircraft;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
    }


}
