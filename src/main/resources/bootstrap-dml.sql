insert into customer(id, username, first_name, last_name, email) values
  (NEXTVAL('customer_id_seq'), 'jevans', 'Jess', 'Evans', 'jevans@test.com');
  
insert into venue(id, name) values
  (NEXTVAL('venue_id_seq'), 'Vegas House of Blues');
  
insert into venue_seating(id, venue_id, level, name, rows, seats_per_row) values
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 1, 'Orchestra', 25, 50),
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 2, 'Main', 20, 100),
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 3, 'Balcony 1', 15, 100),
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 4, 'Balcony 2', 15, 100);
  
insert into event(id, name, venue_id, max_hold_secs, start_dttm, end_dttm) values
  (NEXTVAL('event_id_seq'), 'Kamelot & DragonForce', CURRVAL('venue_id_seq'), 10 * 60,
    PARSEDATETIME('12/7/2015 19:30 PST', 'MM/d/yyyy HH:mm z'), PARSEDATETIME('12/8/2015 03:00 PST', 'MM/d/yyyy HH:mm z'));
  
insert into event_seating(id, event_id, seat_price, venue_seating_id) values
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 100, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 1)),
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 75, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 2)),
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 50, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 3)),
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 40, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 4));


insert into venue(id, name) values
  (NEXTVAL('venue_id_seq'), 'New Orleans Civic Theatre');
  
insert into venue_seating(id, venue_id, level, name, rows, seats_per_row) values
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 1, 'Orchestra', 10, 25),
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 2, 'Reception', 10, 65),
  (NEXTVAL('venue_seating_id_seq'), CURRVAL('venue_id_seq'), 3, 'Banquet', 10, 25);
  
insert into event(id, name, venue_id, max_hold_secs, start_dttm, end_dttm) values
  (NEXTVAL('event_id_seq'), 'Sonata Arctica', CURRVAL('venue_id_seq'), 10 * 60,
    PARSEDATETIME('03/20/2016 20:00 CST', 'MM/d/yyyy HH:mm z'), PARSEDATETIME('03/20/2016 03:00 CST', 'MM/d/yyyy HH:mm z'));
  
insert into event_seating(id, event_id, seat_price, venue_seating_id) values
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 100, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 1)),
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 75, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 2)),
  (NEXTVAL('event_seating_id_seq'), CURRVAL('event_id_seq'), 50, (select id from venue_seating where venue_id = CURRVAL('venue_id_seq') and level = 3));        