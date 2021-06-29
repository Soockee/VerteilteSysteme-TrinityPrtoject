CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE support_ticket
(
    support_ticket_id UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    customer_id       UUID                   NOT NULL,
    status            character varying(255) NOT NULL,
    create_time       TIMESTAMP              NOT NULL DEFAULT now(),

    PRIMARY KEY (support_ticket_id)
);

CREATE TABLE support_ticket_text
(
    support_ticket_text_id UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    support_ticket_id      UUID                   NOT NULL,
    text                   character varying(255) NOT NULL,
    change_time            TIMESTAMP              NOT NULL DEFAULT now(),

    PRIMARY KEY (support_ticket_text_id)
);

INSERT INTO support_ticket (support_ticket_id, customer_id, status)
VALUES ('0f0b4080-d855-11eb-b8bc-0242ac130003', '67b75084-d855-11eb-b8bc-0242ac130003', 'OPEN');
-- open ticket 1
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text)
VALUES ('9e937c40-d855-11eb-b8bc-0242ac130003', '0f0b4080-d855-11eb-b8bc-0242ac130003',
        'Der Kühlschrank brummt sehr laut.');
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text)
VALUES ('9e93805a-d855-11eb-b8bc-0242ac130003', '0f0b4080-d855-11eb-b8bc-0242ac130003',
        'Der Kühlmotor muss ausgetauscht werden.');


INSERT INTO support_ticket (support_ticket_id, customer_id, status)
VALUES ('0f0b42ce-d855-11eb-b8bc-0242ac130003', '67b7543a-d855-11eb-b8bc-0242ac130003', 'OPEN');
-- open ticket 2
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text)
VALUES ('9e938302-d855-11eb-b8bc-0242ac130003', '0f0b42ce-d855-11eb-b8bc-0242ac130003',
        'Der Kühlschrank brummt sehr laut.');


INSERT INTO support_ticket (support_ticket_id, customer_id, status)
VALUES ('0f0b43d2-d855-11eb-b8bc-0242ac130003', '67b75084-d855-11eb-b8bc-0242ac130003', 'CLOSED');
-- closed ticket
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text)
VALUES ('9e93815e-d855-11eb-b8bc-0242ac130003', '0f0b43d2-d855-11eb-b8bc-0242ac130003',
        'Die Tür lässt sich nicht schließen.');
INSERT INTO support_ticket_text (support_ticket_text_id, support_ticket_id, text)
VALUES ('9e938230-d855-11eb-b8bc-0242ac130003', '0f0b43d2-d855-11eb-b8bc-0242ac130003', 'Mach einfach die Tür zu.');