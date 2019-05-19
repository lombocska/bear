CREATE TABLE book(
    id uuid NOT NULL,
    name varchar(255) NOT NULL,
    title varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    sold bigint NOT NULL,
    writer_id uuid NOT NULL,
    category varchar(255) NOT NULL,
    description varchar(1000) NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id),
    CONSTRAINT uc_book_writer_id_name UNIQUE(writer_id, name)
);

CREATE TABLE writer(
    id uuid NOT NULL,
    last_name varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    birth_date date NOT NULL,
    CONSTRAINT pk_writer PRIMARY KEY (id),
    CONSTRAINT uc_writer_id_name UNIQUE(id, last_name)
);


