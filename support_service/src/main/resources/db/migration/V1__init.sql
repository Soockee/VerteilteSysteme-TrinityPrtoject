CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- CREATE TABLE headquarter (
--     id UUID NOT NULL DEFAULT uuid_generate_v1 (),
--     name character varying(255) NOT NULL,
--     description TEXT,

--     PRIMARY KEY(id)
-- );

CREATE TABLE condition
(
    conditions_id         UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    supplier_id           UUID                   NOT NULL,
    part_id               UUID                   NOT NULL,
    price                 NUMERIC(9, 4)          NOT NULL,
    currency              character varying(255) NOT NULL,
    negotiation_timestamp TIMESTAMP                       DEFAULT now(),

    PRIMARY KEY (conditions_id)
);

CREATE TABLE product
(
    product_id      UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    name            character varying(255) NOT NULL,
    production_time INTEGER                NOT NULL,

    PRIMARY KEY (product_id)
);

CREATE TABLE part
(
    part_id UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    name    character varying(255) NOT NULL,

    PRIMARY KEY (part_id)
);

CREATE TABLE product_part
(
    product_part_id UUID    NOT NULL DEFAULT uuid_generate_v1(),
    product_id      UUID    NOT NULL,
    part_id         UUID    NOT NULL,
    count           INTEGER NOT NULL,

    PRIMARY KEY (product_part_id)
);

CREATE TABLE supplier
(
    supplier_id UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    name        character varying(255) NOT NULL,
    PRIMARY KEY (supplier_id)
);

CREATE TABLE orders
(
    order_id    UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    customer_id UUID                   NOT NULL DEFAULT uuid_generate_v1(), -- TODO: Need Customer Managment
    begin_order TIMESTAMP              NOT NULL DEFAULT now(),
    status      character varying(255) NOT NULL,

    PRIMARY KEY (order_id)
);

CREATE TABLE order_product
(
    order_product_id UUID    NOT NULL DEFAULT uuid_generate_v1(),
    order_id         UUID    NOT NULL DEFAULT uuid_generate_v1(),
    product_id       UUID    NOT NULL DEFAULT uuid_generate_v1(),
    count            INTEGER NOT NULL,

    PRIMARY KEY (order_product_id)
);

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