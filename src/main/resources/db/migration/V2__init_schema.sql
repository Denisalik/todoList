CREATE TABLE to_do_items (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    created_by    VARCHAR(100),
    created_date  TIMESTAMP,
    updated_by    VARCHAR(100),
    updated_date  TIMESTAMP,
    version       BIGINT NOT NULL DEFAULT 0
);