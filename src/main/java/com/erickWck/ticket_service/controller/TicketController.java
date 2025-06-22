package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.entity.Ticket;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ticket")
@RestController
public class TicketController {

    @GetMapping("/api/flights")
    public List<Ticket> listAllFlights(){
        return null;
    }

    @GetMapping("/api/flights/{id}")
    public Ticket getFindByIdFlights( long id){
        return null;
    }

    @PostMapping("/api/flights")
    public Ticket createNewFlight(@RequestBody @Valid Ticket ticket){
        return null;
    }

    @PutMapping("/api/flights/{id}")
    public Ticket updateFlightsExist(@PathVariable long id){
        return null;
    }

    @DeleteMapping("/api/flights/{id}")
    public Ticket deleteFlightsExist(@PathVariable long id){
        return null;
    }

}
