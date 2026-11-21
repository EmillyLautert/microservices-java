ALTER TABLE tb_order RENAME COLUMN order_date TO created_at;
ALTER TABLE tb_order ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'UTC';
ALTER TABLE tb_order ADD COLUMN total NUMERIC(12, 2) NOT NULL DEFAULT 0;

ALTER TABLE tb_order_item ADD COLUMN product_title VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE tb_order_item ADD COLUMN product_type VARCHAR(10) NOT NULL DEFAULT 'BOOK';
ALTER TABLE tb_order_item ALTER COLUMN price_at_purchase TYPE NUMERIC(12, 2)
    USING price_at_purchase::NUMERIC(12, 2);
ALTER TABLE tb_order_item ALTER COLUMN currency_at_purchase SET DEFAULT 'BRL';
ALTER TABLE tb_order_item ADD CONSTRAINT ck_order_item_product_type CHECK (product_type IN ('BOOK', 'KIT'));
ALTER TABLE tb_order_item ADD CONSTRAINT ck_order_item_quantity_positive CHECK (quantity > 0);

CREATE TABLE tb_order_item_drawn_book (
    order_item_id INTEGER NOT NULL REFERENCES tb_order_item(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    image VARCHAR(500)
);
