CREATE TABLE restaurant_table (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE timeslot (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation (
    id INTEGER NOT NULL AUTO_INCREMENT,
    reservation_date DATE NOT NULL,
    restaurant_table_id INTEGER NOT NULL,
    timeslot_id INTEGER NOT NULL,
    customer_name VARCHAR(50) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    CONSTRAINT FK_RESERVED_TABLE
        FOREIGN KEY (restaurant_table_id) REFERENCES restaurant_table (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_RESERVED_TIMESLOT
        FOREIGN KEY (timeslot_id) REFERENCES timeslot (id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (id)
);

CREATE INDEX idx_reservation_date ON reservation (reservation_date);
CREATE INDEX idx_reservation_date_table_id_timeslot_id ON reservation (reservation_date, restaurant_table_id, timeslot_id);
CREATE INDEX idx_timeslot_name ON timeslot (name);
CREATE INDEX idx_table_name ON restaurant_table (name);