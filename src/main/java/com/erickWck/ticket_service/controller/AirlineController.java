package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/airline")
@RestController
public class AirlineController {

    @GetMapping("/api/airlines")
    public List<AirlineDtoResponse> listAllAirlines(){
        return null;
    }

    @GetMapping("/api/airlines/{id}")
    public AirlineDtoResponse getFindByIdAirlines(long id){
        return null;
    }

    @PostMapping("/api/airlines")
    public AirlineDtoResponse createNewAirlines(@RequestBody @Valid AirlineDtoRequest airlineRequest){
        return null;
    }

    @PutMapping("/api/airlines/{id}")
    public AirlineDtoResponse updateAirlinesExist(@PathVariable long id){
        return null;
    }

    @DeleteMapping("/api/airlines/{id}")
    public void deleteAirlinesExist(@PathVariable long id){

    }

}
