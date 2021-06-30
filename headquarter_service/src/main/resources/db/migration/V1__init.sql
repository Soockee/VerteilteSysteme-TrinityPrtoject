CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

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
    part_id        UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    name           character varying(255) NOT NULL,
    delievery_time INTEGER                NOT NULL,
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

CREATE TABLE supplier_order_part
(
    supplier_order_part_id UUID    NOT NULL DEFAULT uuid_generate_v1(),
    supplier_order_id      UUID    NOT NULL,
    part_id                UUID    NOT NULL,
    count                  INTEGER NOT NULL,
    PRIMARY KEY (supplier_order_part_id)
);

CREATE TABLE supplier_order
(
    order_id    UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    supplier_id UUID                   NOT NULL DEFAULT uuid_generate_v1(),
    begin_order TIMESTAMP              NOT NULL DEFAULT now(),
    status      character varying(255) NOT NULL,
    PRIMARY KEY (order_id)
);

CREATE TABLE report
(
    report_id       UUID                    NOT NULL DEFAULT uuid_generate_v1 (),
    orders_today    INTEGER                 NOT NULL,
    produced_goods  INTEGER                 NOT NULL,
    production_cost FLOAT                   NOT NULL,
    open_orders     INTEGER                 NOT NULL,
    productivity    FLOAT                   NOT NULL,
    factory_name    character varying(255)  NOT NULL,
    created_at      BIGINT                  NOT NULL,
    PRIMARY KEY(report_id)
);

-- Add suppliers
INSERT INTO supplier (supplier_id, name)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'electroStuff.com');
INSERT INTO supplier (supplier_id, name)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', 'CoolMechanics.com');

-- Add Parts
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa67191c-d4e2-11eb-b8bc-0242ac130003', 'Schraube 3x16', 10000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa67169c-d4e2-11eb-b8bc-0242ac130003', 'Schraube 3x18', 10000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b695e8-d66a-11eb-b8bc-0242ac130003', 'Schraube 4x20', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69ff2-d66a-11eb-b8bc-0242ac130003', 'Kühlbox 60cmx60cmx60cm', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b6a0b0-d66a-11eb-b8bc-0242ac130003', 'Kühlbox Deckel 60cmx60cm', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa671a02-d4e2-11eb-b8bc-0242ac130003', 'Tür Größe S 100cmx60cm', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa671aca-d4e2-11eb-b8bc-0242ac130003', 'Korpus Größe S 100cmx60cmx55cm', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('7f58bfe8-d418-11eb-b8bc-0242ac130003', 'Kühlmotor klein', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b6985e-d66a-11eb-b8bc-0242ac130003', 'Tür Größe M 150cmx60cm', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69944-d66a-11eb-b8bc-0242ac130003', 'Korpus Größe M 150cmx60cmx55cm', 20000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69a0c-d66a-11eb-b8bc-0242ac130003', 'Kühlmotor groß', 30000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69aca-d66a-11eb-b8bc-0242ac130003', 'Tür Größe L 200cmx60cm', 30000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69d4a-d66a-11eb-b8bc-0242ac130003', 'Korpus Größe L 200cmx60cmx55cm', 30000);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69e6c-d66a-11eb-b8bc-0242ac130003', 'Korpus Größe XL 200cmx120cmx55cm', 30000);

-- Add Product
INSERT INTO product (product_id, name, production_time)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', 'Kühlbox 60cmx60cmx60cm - Modell: Die Studentenbox', 10000);
INSERT INTO product (product_id, name, production_time)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003',
        'Kühlschrank Größe S 100cmx60cmx60cm - Modell: Der Kühlschrank für Singles', 20000);
INSERT INTO product (product_id, name, production_time)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003',
        'Kühlschrank Größe M 150cmx60cmx60cm - Modell: Der Kühlschrank für Pärchen', 30000);
INSERT INTO product (product_id, name, production_time)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', 'Kühlschrank Größe L 200cmx60cmx60cm - Modell: Für die ganze Familie',
        30000);
INSERT INTO product (product_id, name, production_time)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003',
        'Kühlschrank Größe XL 200cmx120cmx60cm - Modell: Aus dem Weg Kleinverdiener', 30400);

-- Add Product_Part
INSERT INTO product_part (product_id, part_id, count)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 19);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', '10b69ff2-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', '10b6a0b0-d66a-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 17);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa671a02-d4e2-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa671aca-d4e2-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 19);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', '10b6985e-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', '10b69944-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', 'fa67169c-d4e2-11eb-b8bc-0242ac130003', 23);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', '10b69aca-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', '10b69d4a-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', '10b69a0c-d66a-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b695e8-d66a-11eb-b8bc-0242ac130003', 38);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', 'fa67169c-d4e2-11eb-b8bc-0242ac130003', 9);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b69e6c-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b69aca-d66a-11eb-b8bc-0242ac130003', 2);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b69a0c-d66a-11eb-b8bc-0242ac130003', 1);

-- Add Condition
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 0.10, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'fa67169c-d4e2-11eb-b8bc-0242ac130003', 0.12, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', '10b695e8-d66a-11eb-b8bc-0242ac130003', 0.18, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 50.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', '10b69a0c-d66a-11eb-b8bc-0242ac130003', 79.99, 'EUR');

INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69ff2-d66a-11eb-b8bc-0242ac130003', 19.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b6a0b0-d66a-11eb-b8bc-0242ac130003', 5.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', 'fa671a02-d4e2-11eb-b8bc-0242ac130003', 17.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', 'fa671aca-d4e2-11eb-b8bc-0242ac130003', 59.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b6985e-d66a-11eb-b8bc-0242ac130003', 29.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69944-d66a-11eb-b8bc-0242ac130003', 99.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69aca-d66a-11eb-b8bc-0242ac130003', 59.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69d4a-d66a-11eb-b8bc-0242ac130003', 149.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69e6c-d66a-11eb-b8bc-0242ac130003', 329.99, 'EUR');