CREATE TABLE tb_product (
    id SERIAL NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    description TEXT,
    category VARCHAR(255) NOT NULL,
    type VARCHAR(10) NOT NULL,
    book_count INTEGER,
    price NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    image VARCHAR(255),
    CONSTRAINT ck_product_type CHECK (type IN ('BOOK', 'KIT')),
    CONSTRAINT ck_product_book_count CHECK
        ((type = 'BOOK' AND book_count IS NULL) OR
         (type = 'KIT' AND book_count IS NOT NULL AND book_count BETWEEN 1 AND 5)),
    CONSTRAINT ck_product_author CHECK
        ((type = 'BOOK' AND author IS NOT NULL AND length(trim(author)) > 0) OR
         (type = 'KIT' AND author IS NULL))
);

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
('Kit Terror', NULL, 'Três livros de terror escolhidos de forma surpresa no checkout.', 'Terror', 'KIT', 3, 69.90, 'BRL', NULL),
('Kit Fantasia', NULL, 'Três livros de fantasia escolhidos de forma surpresa no checkout.', 'Fantasia', 'KIT', 3, 79.90, 'BRL', NULL),
('Kit Romance', NULL, 'Três romances escolhidos de forma surpresa no checkout.', 'Romance', 'KIT', 3, 59.90, 'BRL', NULL),
('Kit Ficção científica', NULL, 'Três livros de ficção científica escolhidos no checkout.', 'Ficção científica', 'KIT', 3, 74.90, 'BRL', NULL),
('Kit Suspense', NULL, 'Três livros de suspense escolhidos de forma surpresa no checkout.', 'Suspense', 'KIT', 3, 64.90, 'BRL', NULL),
('O Iluminado', 'Stephen King', 'Um hotel isolado e uma família em perigo durante o inverno.', 'Terror', 'BOOK', NULL, 49.90, 'BRL', NULL),
('O Nome do Vento', 'Patrick Rothfuss', 'A história de Kvothe e sua jornada pelo mundo da magia.', 'Fantasia', 'BOOK', NULL, 59.90, 'BRL', NULL),
('Emma', 'Jane Austen', 'Uma jovem bem-intencionada tenta organizar a vida amorosa de seus amigos.', 'Romance', 'BOOK', NULL, 39.90, 'BRL', NULL),
('2001: Uma Odisseia no Espaço', 'Arthur C. Clarke', 'Uma viagem espacial que explora a evolução humana e a inteligência artificial.', 'Ficção científica', 'BOOK', NULL, 49.90, 'BRL', NULL),
('O Código Da Vinci', 'Dan Brown', 'Um simbologista investiga pistas ocultas em uma trama de mistério.', 'Suspense', 'BOOK', NULL, 44.90, 'BRL', NULL);
