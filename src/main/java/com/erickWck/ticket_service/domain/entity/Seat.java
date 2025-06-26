package com.erickWck.ticket_service.domain.entity;

//@Entity
//@Table(name = "aircraft_seats")
public class Seat {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; // ex: "12A"

//    @ManyToOne
//    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

}
