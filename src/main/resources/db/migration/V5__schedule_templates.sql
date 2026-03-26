-- V5__schedule_templates.sql

-- Wipe existing trip/ticket data (dev only) 
TRUNCATE tickets CASCADE; 
TRUNCATE schedules CASCADE;

-- Create schedule_templates FIRST 
(schedules will reference it) CREATE TABLE 
schedule_templates (
    id VARCHAR(50) PRIMARY KEY, 
    route_id VARCHAR(50) NOT NULL,
    bus_id VARCHAR(50) NOT NULL, 
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    price FLOAT NOT NULL, 
    days_of_week VARCHAR(100) NOT NULL, 
    active BOOLEAN NOT NULL DEFAULT TRUE, 
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    
    CONSTRAINT fk_template_route
        FOREIGN KEY (route_id) REFERENCES routes(id),
    
    CONSTRAINT fk_template_bus 
	FOREIGN KEY (bus_id) REFERENCES buses(id)
); -- Add template FK to schedules 

ALTER TABLE schedules 
ADD COLUMN schedule_template_id VARCHAR(50), 
ADD CONSTRAINT fk_schedule_template
    FOREIGN KEY (schedule_template_id) 
    REFERENCES schedule_templates(id) 
    ON DELETE SET NULL;

-- Index for fast trip lookup by template 
CREATE INDEX idx_schedule_template ON schedules(schedule_template_id); 

-- Add CANCELLED to schedule_status enum if not already there 
ALTER TYPE schedule_status ADD VALUE IF NOT EXISTS 'CANCELLED';
