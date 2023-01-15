DROP EXTENSION IF EXISTS "uuid-ossp" cascade;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS articles cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS comments cascade;

CREATE TABLE articles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL UNIQUE,
    content TEXT NOT NULL,
    thumbnail TEXT DEFAULT NULL,
    local_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    avatar TEXT DEFAULT NULL
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    article_id UUID REFERENCES articles(id) NOT NULL,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    content TEXT NOT NULL,
    local_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO articles (id, title, content, thumbnail, local_date_time) VALUES
    ('40e6215d-b5c6-4896-987c-f30f3678f608',
     'QAnon, adrift after Trump’s defeat, finds new life in Elon Musk’s Twitter',
     '',
     'thumbnail.webp',
     '2022-12-25T00:40:23'),

    ('6ecd8c99-4036-403d-bf84-cf8400f67836',
     'For new NCI director, work turns personal: She is diagnosed with cancer',
     '',
     'thumbnail.webp',
     '2022-12-25T00:40:23'),

    ('3f333df6-90a4-4fda-8dd3-9485d27cee36',
     'In a new cocktail book, New Orleans has a Cure for what ails you',
     '',
     'thumbnail.webp',
     '2022-12-25T00:40:23'),

    ('0e37df36-f698-11e6-8dd4-cb9ced3df976',
     'Kale salad with squash and pomegranate is a feast for the eyes',
     '',
     'thumbnail.webp',
     '2022-12-25T00:40:23');

-- password = password123
INSERT INTO users (email, password, role, avatar) VALUES
    ('user1@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER', 'avatar.svg'),
    ('user2@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER', 'avatar.svg'),
    ('user3@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER', NULL),
    ('moderator1@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_MODERATOR', NULL),
    ('moderator2@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_MODERATOR', NULL),
    ('admin@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_ADMIN', NULL);

INSERT INTO comments (article_id, user_id, content, local_date_time) VALUES
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 1, 'Good news', '2022-12-25T00:40:23'),
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 2, 'Hello there', '2022-12-25T00:40:23'),
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 3, 'Hi', '2022-12-25T00:40:23'),
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 4, 'Hello', '2022-12-25T00:40:23'),
    ('6ecd8c99-4036-403d-bf84-cf8400f67836', 1, 'Interesting', '2022-12-25T00:40:23');
