SET
SEARCH_PATH = testcontainer;

CREATE TABLE person
(
    id    serial PRIMARY KEY,
    name  varchar(255),
    email varchar(255)
);
