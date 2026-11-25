ALTER TABLE tb_product DROP CONSTRAINT ck_product_book_count;
ALTER TABLE tb_product ADD CONSTRAINT ck_product_book_count CHECK
    ((type = 'BOOK' AND book_count IS NULL) OR
     (type = 'KIT' AND book_count IS NOT NULL AND book_count BETWEEN 1 AND 5));
