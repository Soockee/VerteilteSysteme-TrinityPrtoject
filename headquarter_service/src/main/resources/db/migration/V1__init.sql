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
    price NUMERIC(9,4) NOT NULL,
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