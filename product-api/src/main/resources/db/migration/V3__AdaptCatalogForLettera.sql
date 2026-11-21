-- Preserve o catálogo antigo para permitir recuperação dos dados fora de domínio.
CREATE TABLE tb_product_legacy AS TABLE tb_product;

-- O catálogo ativo passa a representar livros e kits em vez dos celulares de demonstração.
ALTER TABLE tb_product RENAME COLUMN brand TO author;
ALTER TABLE tb_product RENAME COLUMN model TO title;
ALTER TABLE tb_product ALTER COLUMN currency TYPE VARCHAR(3);
ALTER TABLE tb_product ALTER COLUMN price TYPE NUMERIC(12, 2) USING price::NUMERIC(12, 2);
ALTER TABLE tb_product ADD COLUMN category VARCHAR(80);
ALTER TABLE tb_product ADD COLUMN type VARCHAR(10);
ALTER TABLE tb_product ADD COLUMN book_count INTEGER;

TRUNCATE TABLE tb_product RESTART IDENTITY;

ALTER TABLE tb_product ALTER COLUMN title SET NOT NULL;
ALTER TABLE tb_product ALTER COLUMN category SET NOT NULL;
ALTER TABLE tb_product ALTER COLUMN type SET NOT NULL;
ALTER TABLE tb_product ADD CONSTRAINT ck_product_type CHECK (type IN ('BOOK', 'KIT'));
ALTER TABLE tb_product ADD CONSTRAINT ck_product_book_count CHECK
    ((type = 'BOOK' AND book_count IS NULL) OR (type = 'KIT' AND book_count > 0));

INSERT INTO tb_product (title, author, description, category, type, book_count, price, currency, image) VALUES
('Drácula', 'Bram Stoker', 'O clássico romance gótico que acompanha a chegada do Conde Drácula à Inglaterra.', 'Terror', 'BOOK', NULL, 44.90, 'BRL', NULL),
('Frankenstein', 'Mary Shelley', 'Uma obra pioneira sobre criação, responsabilidade e os limites da ciência.', 'Terror', 'BOOK', NULL, 39.90, 'BRL', NULL),
('O Hobbit', 'J. R. R. Tolkien', 'Bilbo Bolseiro parte em uma aventura inesperada pela Terra-média.', 'Fantasia', 'BOOK', NULL, 49.90, 'BRL', NULL),
('A Sociedade do Anel', 'J. R. R. Tolkien', 'O início da jornada para destruir o Um Anel.', 'Fantasia', 'BOOK', NULL, 59.90, 'BRL', NULL),
('Orgulho e Preconceito', 'Jane Austen', 'Uma história de encontros, escolhas e relações sociais.', 'Romance', 'BOOK', NULL, 34.90, 'BRL', NULL),
('Persuasão', 'Jane Austen', 'Um romance sobre reencontros e segundas oportunidades.', 'Romance', 'BOOK', NULL, 32.90, 'BRL', NULL),
('Duna', 'Frank Herbert', 'Uma saga de ficção científica ambientada no desértico planeta Arrakis.', 'Ficção científica', 'BOOK', NULL, 54.90, 'BRL', NULL),
('Eu, Robô', 'Isaac Asimov', 'Contos que exploram as relações entre humanos e inteligência artificial.', 'Ficção científica', 'BOOK', NULL, 42.90, 'BRL', NULL),
('O Cão dos Baskerville', 'Arthur Conan Doyle', 'Sherlock Holmes investiga uma antiga lenda e uma morte misteriosa.', 'Suspense', 'BOOK', NULL, 37.90, 'BRL', NULL),
('E Não Sobrou Nenhum', 'Agatha Christie', 'Dez desconhecidos ficam isolados e passam a ser acusados um a um.', 'Suspense', 'BOOK', NULL, 46.90, 'BRL', NULL),
('Kit Terror', NULL, 'Dois livros de terror escolhidos de forma surpresa no checkout.', 'Terror', 'KIT', 2, 69.90, 'BRL', NULL),
('Kit Fantasia', NULL, 'Dois livros de fantasia escolhidos de forma surpresa no checkout.', 'Fantasia', 'KIT', 2, 79.90, 'BRL', NULL),
('Kit Romance', NULL, 'Dois romances escolhidos de forma surpresa no checkout.', 'Romance', 'KIT', 2, 59.90, 'BRL', NULL),
('Kit Ficção científica', NULL, 'Dois livros de ficção científica escolhidos no checkout.', 'Ficção científica', 'KIT', 2, 74.90, 'BRL', NULL),
('Kit Suspense', NULL, 'Dois livros de suspense escolhidos de forma surpresa no checkout.', 'Suspense', 'KIT', 2, 64.90, 'BRL', NULL);
