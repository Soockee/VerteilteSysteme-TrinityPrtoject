CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE central (
    id UUID NOT NULL DEFAULT uuid_generate_v1 (),
    name character varying(255) NOT NULL,
    description TEXT,

    PRIMARY KEY(id)
);
