DELETE FROM support_ticket;
DELETE FROM support_ticket_text;

INSERT INTO support_ticket (support_ticket_id, customer_id, status, create_time)
VALUES ("0f0b4080-d855-11eb-b8bc-0242ac130003", "67b75084-d855-11eb-b8bc-0242ac130003", "open", 1624914712);
INSERT INTO support_ticket (support_ticket_id, customer_id, status, create_time)
VALUES ("0f0b42ce-d855-11eb-b8bc-0242ac130003", "67b7543a-d855-11eb-b8bc-0242ac130003", "open", 1624864312);
INSERT INTO support_ticket (support_ticket_id, customer_id, status, create_time)
VALUES ("0f0b43d2-d855-11eb-b8bc-0242ac130003", "67b75084-d855-11eb-b8bc-0242ac130003", "closed", 1622210752);

-- open ticket 1
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text, change_time)
VALUES ("9e937c40-d855-11eb-b8bc-0242ac130003", "0f0b4080-d855-11eb-b8bc-0242ac130003", "Der Kühlschrank brummt sehr laut.", 1624914712)
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text, change_time)
VALUES ("9e93805a-d855-11eb-b8bc-0242ac130003", "0f0b4080-d855-11eb-b8bc-0242ac130003", "Der Kühlmotor muss ausgetauscht werden.", 1624953952)

-- open ticket 2
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text, change_time)
VALUES ("9e938302-d855-11eb-b8bc-0242ac130003", "0f0b42ce-d855-11eb-b8bc-0242ac130003", "Der Kühlschrank brummt sehr laut.", 1624864312)

-- closed ticket
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text, change_time)
VALUES ("9e93815e-d855-11eb-b8bc-0242ac130003", "0f0b43d2-d855-11eb-b8bc-0242ac130003", "Die Tür lässt sich nicht schließen.", 1622210752)
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text, change_time)
VALUES ("9e938230-d855-11eb-b8bc-0242ac130003", "0f0b43d2-d855-11eb-b8bc-0242ac130003", "Mach einfach die Tür zu.", 1622793952)