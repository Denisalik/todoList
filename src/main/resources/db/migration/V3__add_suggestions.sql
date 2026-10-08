CREATE TABLE to_do_suggestions (
    id BIGSERIAL PRIMARY KEY,
    to_do_item_id BIGINT NOT NULL UNIQUE REFERENCES to_do_items(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    model VARCHAR(255) NOT NULL,

    created_by VARCHAR(255),
    created_date TIMESTAMP,
    updated_by VARCHAR(255),
    updated_date TIMESTAMP,
    version BIGINT
);