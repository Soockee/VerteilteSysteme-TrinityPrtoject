CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE headquarter (
    id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    description TEXT,

    PRIMARY KEY(id)
);

CREATE TABLE condition (
    conditionsId UUID NOT NULL DEFAULT uuid_generate_v1 (),
    suppliedId UUID NOT NULL,
    price NUMERIC(9,4),
    negotioationTimestamp: TIMESTAMP,
    PRIMARY KEY(conditionsId)
);