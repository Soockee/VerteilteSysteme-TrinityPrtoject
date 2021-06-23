CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE headquarter (
    id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    description TEXT,

    PRIMARY KEY(id)
);

CREATE TABLE condition (
    conditions_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    supplier_id UUID NOT NULL,
    part_supplier_id UUID NOT NULL,
    price NUMERIC(9,4) NOT NULL,
    currency character varying(255) NOT NULL,
    negotiation_timestamp TIMESTAMP DEFAULT now (),

    PRIMARY KEY(conditions_id)
);

CREATE TABLE product (
    product_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    productionTime INTEGER NOT NULL,
    parts JSON NOT NULL,

    PRIMARY KEY(product_id)
);

CREATE TABLE part (
    part_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    supplier_id UUID NOT NULL,
    supplier_part_id UUID NOT NULL,
    PRIMARY KEY(part_id)
);

CREATE TABLE supplier (
    supplier_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    PRIMARY KEY(supplier_id)
);

CREATE TABLE supplier_part (
    supplier_part_id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    part_id UUID NOT NULL,
    supplier_id integer NOT NULL,
    name character varying(255) NOT NULL,
    PRIMARY KEY(supplier_part_id)
);


INSERT INTO supplier (supplier_id,name)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'electroStuff.com');
INSERT INTO supplier (supplier_id,name)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003', 'CoolMechanics.com');

INSERT INTO supplier_part (part_id,supplier_id,name)
VALUES('7f58bc28-d418-11eb-b8bc-0242ac130003', 0 ,'Schraube');
INSERT INTO supplier_part (part_id,supplier_id,name)
VALUES('7f58be62-d418-11eb-b8bc-0242ac130003', 0 ,'Mobs');
INSERT INTO supplier_part (part_id,supplier_id,name)
VALUES('7f58bfe8-d418-11eb-b8bc-0242ac130003', 1 ,'keks');
INSERT INTO supplier_part (part_id,supplier_id,name)
VALUES('93338fb6-d418-11eb-b8bc-0242ac130003', 1 ,'Honig');


INSERT INTO condition (supplier_id,part_supplier_id,price,currency)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58be62-d418-11eb-b8bc-0242ac130003', 0.001 ,'Euro');

INSERT INTO condition (supplier_id,part_supplier_id,price,currency)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58bc28-d418-11eb-b8bc-0242ac130003', 110.5,'Euro');



INSERT INTO condition (supplier_id,part_supplier_id,price,currency)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003','7f58bfe8-d418-11eb-b8bc-0242ac130003', 1.55,'USD');
INSERT INTO condition (supplier_id,part_supplier_id,price,currency)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58bfe8-d418-11eb-b8bc-0242ac130003', 1.70,'USD');
INSERT INTO condition (supplier_id,part_supplier_id,price,currency)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003','93338fb6-d418-11eb-b8bc-0242ac130003', 20.0,'USD');

/*
    Parts:
    2x Same Supplier different part
    1x different Supplier same part
*/
INSERT INTO part (supplier_id,supplier_part_id)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003','7f58bfe8-d418-11eb-b8bc-0242ac130003');
INSERT INTO part (supplier_id,supplier_part_id)
VALUES('8d017b98-d419-11eb-b8bc-0242ac130003','93338fb6-d418-11eb-b8bc-0242ac130003');

INSERT INTO part (supplier_id,supplier_part_id)
VALUES('8a34a8fe-d419-11eb-b8bc-0242ac130003','7f58bfe8-d418-11eb-b8bc-0242ac130003');