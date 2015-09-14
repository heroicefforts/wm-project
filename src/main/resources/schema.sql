CREATE SEQUENCE customer_id_seq;
CREATE TABLE customer (
  id integer not null primary key,
  username varchar(75) not null,
  first_name varchar(100) not null,
  last_name varchar(100) not null,
  email varchar(250) not null
);
CREATE UNIQUE INDEX customer_username_uniq ON customer(username);
CREATE UNIQUE INDEX customer_email_uniq ON customer(email);


CREATE SEQUENCE venue_id_seq;
CREATE TABLE venue (
  id integer not null,
  name varchar(250) not null,
  primary key(id)
);
CREATE UNIQUE INDEX venue_name_uniq ON venue(name);


CREATE SEQUENCE venue_seating_id_seq;
CREATE TABLE venue_seating (
  id integer not null,
  venue_id integer not null references venue(id),
  level integer not null,
  name varchar(250) not null,
  rows integer not null,
  seats_per_row integer not null,
  primary key(id)
);
CREATE UNIQUE INDEX venue_seating_nat_idx ON venue_seating(venue_id, level);


CREATE SEQUENCE event_id_seq;
CREATE TABLE event (
  id integer not null,
  name varchar(250) not null,
  start_dttm timestamp not null,
  end_dttm timestamp not null,
  max_hold_secs numeric(7) not null,
  venue_id integer not null references venue(id),
  primary key(id)
);
CREATE UNIQUE INDEX event_nat_idx ON event(name, start_dttm);


CREATE SEQUENCE event_seating_id_seq;
CREATE TABLE event_seating (
  id integer not null,
  event_id integer not null references event(id),
  venue_seating_id integer not null references venue_seating(id),
  seat_price numeric(7,2) not null,
  primary key(id)
);
CREATE UNIQUE INDEX event_seating_nat_idx ON event_seating(event_id, venue_seating_id);


CREATE SEQUENCE seat_hold_id_seq;
CREATE TABLE seat_hold (
  id integer not null,
  customer_id integer not null references customer(id),
  guid varchar(250) not null,
  confirmation varchar(250),
  crte_dttm timestamp not null,
  lease_count integer not null,
  primary key(id)
);  
CREATE UNIQUE INDEX seat_hold_nat_idx ON seat_hold(customer_id, guid);


CREATE SEQUENCE seat_lease_id_seq;
CREATE TABLE seat_lease (
  id integer not null,
  event_seating_id integer not null references event_seating(id),
  customer_id integer not null references customer(id),
  expiration_dttm timestamp not null,
  seats integer not null,
  primary key(id)
);


CREATE TABLE seat_hold_lease (
  seat_hold_id integer not null references seat_hold(id),
  seat_lease_id integer not null references seat_lease(id),
  primary key(seat_hold_id, seat_lease_id)
);
