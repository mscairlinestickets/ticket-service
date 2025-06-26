package com.erickWck.ticket_service.domain.service.flightFunctions;

import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.exception.NoAvailableSeatsException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlightFunctions {


    public static boolean hasAvailableSeats(Flight flight) {
        return flight.getAvailableSeats() > 0;
    }

    public static void decrementAvailableSeats(Flight flight) {

        if (!hasAvailableSeats(flight)) {
            throw new NoAvailableSeatsException(flight.getFlightNumber());
        }
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
    }

}
