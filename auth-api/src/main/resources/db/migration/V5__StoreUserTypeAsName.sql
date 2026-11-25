ALTER TABLE tb_user DROP CONSTRAINT IF EXISTS tb_user_type_check;
ALTER TABLE tb_user ALTER COLUMN type TYPE VARCHAR(16)
    USING CASE type WHEN 0 THEN 'Admin' WHEN 1 THEN 'Common' END;
ALTER TABLE tb_user ALTER COLUMN type SET NOT NULL;
ALTER TABLE tb_user ADD CONSTRAINT ck_user_type CHECK (type IN ('Admin', 'Common'));
