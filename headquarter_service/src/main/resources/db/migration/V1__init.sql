CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- CREATE TABLE headquarter (
--     id UUID NOT NULL DEFAULT uuid_generate_v1 (),
--     name character varying(255) NOT NULL,
--     description TEXT,

--     PRIMARY KEY(id)
-- );

CREATE TABLE condition (
    conditions_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    supplier_id UUID NOT NULL,
    part_id UUID NOT NULL,
    price NUMERIC(9,4) NOT NULL,
    currency character varying(255) NOT NULL,
    negotiation_timestamp TIMESTAMP DEFAULT now(),

    PRIMARY KEY(conditions_id)
);

CREATE TABLE product (
    product_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    production_time INTEGER NOT NULL,

    PRIMARY KEY(product_id)
);

CREATE TABLE part (
    part_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,

    PRIMARY KEY(part_id)
);

CREATE TABLE product_part (
    product_part_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    product_id UUID NOT NULL,
    part_id UUID NOT NULL,
    count INTEGER NOT NULL,

    PRIMARY KEY(product_part_id)
);

CREATE TABLE supplier (
    supplier_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    PRIMARY KEY(supplier_id)
);

CREATE TABLE orders (
    order_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    customer_id UUID NOT NULL DEFAULT uuid_generate_v1 (), -- TODO: Need Customer Managment
    begin_order TIMESTAMP NOT NULL DEFAULT now(),
    status character varying(255) NOT NULL,

    PRIMARY KEY (order_id)
);

CREATE TABLE order_product (
    order_product_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    order_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    product_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    count INTEGER NOT NULL,

    PRIMARY KEY(order_product_id)
);

CREATE TABLE support_ticket (
    support_ticket_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    customer_id UUID NOT NULL,
    status character varying(255) NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT now(),

    PRIMARY KEY(support_ticket_id)
);

CREATE TABLE support_ticket_text (
    support_ticket_text_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    support_ticket_id UUID NOT NULL,
    text character varying(255) NOT NULL,
    change_time TIMESTAMP NOT NULL DEFAULT now(),

    PRIMARY KEY (support_ticket_text_id)
);



INSERT INTO supplier (supplier_id,name)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'electroStuff.com');
INSERT INTO supplier (supplier_id,name)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003', 'CoolMechanics.com');

-- Add Parts
INSERT INTO part (part_id,name)
VALUES('fa67169c-d4e2-11eb-b8bc-0242ac130003','Schraube 3x18');
INSERT INTO part (part_id,name)
VALUES('fa67191c-d4e2-11eb-b8bc-0242ac130003','Schraube 3x16');
INSERT INTO part (part_id,name)
VALUES('fa671a02-d4e2-11eb-b8bc-0242ac130003','Tür 100x60');
INSERT INTO part (part_id,name)
VALUES('fa671aca-d4e2-11eb-b8bc-0242ac130003','Korpus 100x60x55');
INSERT INTO part (part_id,name)
VALUES('7f58bfe8-d418-11eb-b8bc-0242ac130003','Kühlmotor');

-- Add Product
INSERT INTO product (product_id, name, production_time)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003','Kühlschrank Größe S 100x60x60', 100);
INSERT INTO product (product_id, name, production_time)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003','Kühlschrank Größe M 150x60x60', 200);
INSERT INTO product (product_id, name, production_time)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003','Kühlschrank Größe L 200x60x60 Modell: Aus dem Weg Kleinverdiener', 300);

-- Add Product_Part
-- Kühlschrank Größe S 100x60x60
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa671aca-d4e2-11eb-b8bc-0242ac130003', 1); -- Korpus
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa671a02-d4e2-11eb-b8bc-0242ac130003', 1); -- Tür
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 1); -- Kühlmotor
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 10); -- Schraube 3x16


-- Add Condition

INSERT INTO condition (supplier_id,part_id,price,currency)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58be62-d418-11eb-b8bc-0242ac130003', 0.001 ,'Euro');

INSERT INTO condition (supplier_id,part_id,price,currency)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58bc28-d418-11eb-b8bc-0242ac130003', 110.5,'Euro');

INSERT INTO condition (supplier_id,part_id,price,currency)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003','7f58bfe8-d418-11eb-b8bc-0242ac130003', 1.55,'USD');
INSERT INTO condition (supplier_id,part_id,price,currency)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58bfe8-d418-11eb-b8bc-0242ac130003', 1.70,'USD');
INSERT INTO condition (supplier_id,part_id,price,currency)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003','93338fb6-d418-11eb-b8bc-0242ac130003', 20.0,'USD');
