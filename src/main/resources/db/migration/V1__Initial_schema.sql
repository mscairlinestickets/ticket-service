CREATE TABLE airlines (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    icao_code VARCHAR(10) NOT NULL UNIQUE,
    created_date        timestamp NOT NULL,
    last_modified_date  timestamp NOT NULL,
    version             integer NOT NULL
);

CREATE TABLE aircraft (
    uuid BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    seat_capacity INTEGER NOT NULL CHECK (seat_capacity > 0),
    created_date        timestamp NOT NULL,
    last_modified_date  timestamp NOT NULL,
    version             integer NOT NULL
);

CREATE TABLE flight (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    flight_number VARCHAR(20) NOT NULL,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_date_time TIMESTAMP NOT NULL,
    total_seats INTEGER NOT NULL CHECK (total_seats > 0),
    available_seats INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    airline BIGINT NOT NULL,
    aircraft BIGINT NOT NULL,
    created_date        timestamp NOT NULL,
    last_modified_date  timestamp NOT NULL,
    version             integer NOT NULL,

    CONSTRAINT fk_flight_airline FOREIGN KEY (airline) REFERENCES airlines(id),
    CONSTRAINT fk_flight_aircraft FOREIGN KEY (aircraft) REFERENCES aircraft(uuid)
);
