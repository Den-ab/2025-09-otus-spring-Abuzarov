insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(content, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3);

-- Хеш указан для пароля 123 (через BCrypt)
insert into users(login, password)
values ('test.login', '$2a$12$vx3lV6uUn/RN.32k/y0UyOeOGmRoRgsnh3o9CWS8M5ik1fjoTckoW');
