-- =========================
-- ENUM TYPES
-- =========================
CREATE TYPE bus_status AS ENUM ('ACTIVE', 'MAINTENANCE');

CREATE TYPE schedule_status AS ENUM (
    'SCHEDULED',
    'BOARDING',
    'IN_TRANSIT',
    'ARRIVED'
);

CREATE TYPE ticket_status AS ENUM (
    'BOOKED',
    'CANCELLED',
    'USED'
);

-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- BUSES
-- =========================
CREATE TABLE buses (
    id VARCHAR(50) PRIMARY KEY,
    plate_number VARCHAR(50) NOT NULL UNIQUE,
    capacity INT NOT NULL CHECK (capacity > 0),
    status "bus_status" NOT NULL 
);

-- =========================
-- ROUTES
-- =========================
CREATE TABLE routes (
    id VARCHAR(50) PRIMARY KEY,
    origin_name VARCHAR(150) NOT NULL,
    destination_name VARCHAR(150) NOT NULL,
    distance_km FLOAT,
    estimated_duration_min INT
);

-- =========================
-- ROUTE POINTS (path/stops)
-- =========================
CREATE TABLE route_points (
    id VARCHAR(50) PRIMARY KEY,
    route_id VARCHAR(50) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    sequence_order INT NOT NULL,
    stop_name VARCHAR(150),

    CONSTRAINT fk_routepoints_route
        FOREIGN KEY (route_id)
        REFERENCES routes(id)
        ON DELETE CASCADE
);

-- =========================
-- SCHEDULES (actual trips)
-- =========================
CREATE TABLE schedules (
    id VARCHAR(50) PRIMARY KEY,
    route_id VARCHAR(50) NOT NULL,
    bus_id VARCHAR(50) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    price FLOAT NOT NULL,
    status "schedule_status" NOT NULL,

    CONSTRAINT fk_schedule_route
        FOREIGN KEY (route_id)
        REFERENCES routes(id),

    CONSTRAINT fk_schedule_bus
        FOREIGN KEY (bus_id)
        REFERENCES buses(id)
);

-- =========================
-- TICKETS (seat bookings)
-- =========================
CREATE TABLE tickets (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    schedule_id VARCHAR(50) NOT NULL,
    seat_number INT NOT NULL,
    booking_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status "ticket_status" NOT NULL,

    CONSTRAINT fk_ticket_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_ticket_schedule
        FOREIGN KEY (schedule_id)
        REFERENCES schedules(id)
);

-- =========================
-- INDEXES FOR PERFORMANCE
-- =========================
CREATE INDEX idx_schedule_route ON schedules(route_id);
CREATE INDEX idx_schedule_bus ON schedules(bus_id);
CREATE INDEX idx_ticket_user ON tickets(user_id);
CREATE INDEX idx_ticket_schedule ON tickets(schedule_id);
CREATE INDEX idx_routepoints_route ON route_points(route_id);