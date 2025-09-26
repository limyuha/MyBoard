-- User 더미 데이터
INSERT INTO users (email, password, name, role)
VALUES ('test@example.com', '1234', '테스트유저', 'USER');  -- id 값은 자동증가이기 때문에 안넣고 실행 id 값은 1

INSERT INTO users (email, password, name, role)
VALUES ('yuha@naver.com',
        '$2a$10$957G.24RS8dyx0YHQNSC1eHc6PlPYPiSMx/faTYRvgFvcmH3oZyrS',  -- "1"을 Bcrypt로 암호화
        '임유하',
        'USER');

-- Article 더미 데이터 (created_at 추가!)
INSERT INTO article (title, content, author_id, views, created_at)
--VALUES (1, '테스트 글 1', '첫 번째 내용입니다.', 1, CURRENT_TIMESTAMP);
VALUES ('테스트 글 1', '첫 번째 내용입니다.', 1, 0, '2025-08-20 15:00:00');

--INSERT INTO article (id, title, content, author_id, created_at)
INSERT INTO article (title, content, author_id, views, created_at)
VALUES ('테스트 글 2', '두 번째 내용입니다.', 1, 0, '2025-08-20 15:30:00');

INSERT INTO article (title, content, author_id, views, created_at)
VALUES ('테스트 글 3', '세 번째 내용입니다.', 1, 0, CURRENT_TIMESTAMP);

INSERT INTO article (title, content, author_id, views, created_at)
VALUES ('테스트 글 4', '네 번째 내용입니다.', 1, 0, CURRENT_TIMESTAMP);

INSERT INTO article (title, content, author_id, views, created_at)
VALUES ('테스트 글 5', '다섯 번째 내용입니다.', 1, 0, CURRENT_TIMESTAMP);

INSERT INTO article (title, content, author_id, views, created_at)
VALUES ('테스트 글 6', '여섯 번째 내용입니다.', 1, 0, CURRENT_TIMESTAMP);

