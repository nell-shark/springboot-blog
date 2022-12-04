DROP TABLE IF EXISTS articles cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS comments cascade;

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    content TEXT NOT NULL,
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

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    article_id BIGINT REFERENCES articles(id) NOT NULL,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    content TEXT NOT NULL,
    published DATE NOT NULL
);

INSERT INTO articles (title, content, published, image) VALUES
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

INSERT INTO comments (article_id, user_id, content, published) VALUES
    (1, 1, 'Good news', '2022-10-22'),
    (1, 2, 'Hello there', '2022-9-22'),
    (1, 3, 'Hi', '2022-8-22'),
    (1, 4, 'Hello', '2022-7-22'),
    (2, 1, 'Interesting', '2022-6-22');
