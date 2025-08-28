package com.erickWck.ticket_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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

    @Version
    private int version;
}
