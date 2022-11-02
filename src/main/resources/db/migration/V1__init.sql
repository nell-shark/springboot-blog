DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users;

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    text TEXT NOT NULL,
    published DATE NOT NULL,
    image TEXT
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    image TEXT
);

INSERT INTO articles (title, text, published, image) VALUES
    ('Wet Leg, Nova Twins and Kojey Radical on the healing power of music', '', '2022-10-22', 'img1.webp'),
    ('Xi Jinping''s party is just getting started', '', '2022-10-23', 'img2.webp'),
    ('How a magician-mathematician revealed a casino loophole', '', '2022-10-24', 'img3.webp'),
    ('A fish that sparked a national obsession', '', '2022-10-25', 'img4.webp');


-- password = password123
INSERT INTO users (email, password, role) VALUES
    ('user1@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER'),
    ('user2@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER'),
    ('user3@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER'),
    ('admin1@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_ADMIN'),
    ('admin2@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_ADMIN');
