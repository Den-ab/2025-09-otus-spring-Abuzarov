insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(content, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3);

-- Хеш указан для пароля 123 (через BCrypt)
insert into users(id, login, password, role) values
    (1, 'test.observer', '$2a$12$vx3lV6uUn/RN.32k/y0UyOeOGmRoRgsnh3o9CWS8M5ik1fjoTckoW', 'ROLE_USER_OBSERVER'),
    (2, 'test.editor', '$2a$12$vx3lV6uUn/RN.32k/y0UyOeOGmRoRgsnh3o9CWS8M5ik1fjoTckoW', 'ROLE_USER_EDITOR'),
    (3, 'test.admin', '$2a$12$vx3lV6uUn/RN.32k/y0UyOeOGmRoRgsnh3o9CWS8M5ik1fjoTckoW', 'ROLE_SUPER_ADMIN');

insert into acl_sid (id, principal, sid) values
    (1, 0, 'ROLE_USER_OBSERVER'),
    (2, 0, 'ROLE_USER_EDITOR'),
    (3, 0, 'ROLE_SUPER_ADMIN');

insert into acl_class (id, class) values
    (1, 'ru.otus.hw.models.Book'),
    (2, 'ru.otus.hw.models.Genre'),
    (3, 'ru.otus.hw.models.Author'),
    (4, 'ru.otus.hw.models.Comment');

insert into acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) values
    (1, 1, 1, NULL, 3, 0),
    (2, 1, 2, NULL, 3, 0),
    (3, 1, 3, NULL, 3, 0),
    (4, 2, 1, NULL, 3, 0),
    (5, 2, 2, NULL, 3, 0),
    (6, 2, 3, NULL, 3, 0),
    (7, 3, 1, NULL, 3, 0),
    (8, 3, 2, NULL, 3, 0),
    (9, 3, 3, NULL, 3, 0),
    (10, 4, 1, NULL, 3, 0),
    (11, 4, 2, NULL, 3, 0),
    (12, 4, 3, NULL, 3, 0);

-- маска 1 - только чтение
-- маска 11 - это CRUD (чтение + ред + удаление): 1 + 2 + 8
-- маска 16 - это админские: 16
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    -- Book ID = 1
    (1, 0, 1, 1, 1, 1, 1),
    (1, 1, 2, 11, 1, 1, 1),
    (1, 2, 3, 16, 1, 1, 1),
    -- Book ID = 2
    (2, 0, 1, 1, 1, 1, 1),
    (2, 1, 2, 11, 1, 1, 1),
    (2, 2, 3, 16, 1, 1, 1),
    -- Book ID = 3
    (3, 0, 1, 1, 1, 1, 1),
    (3, 1, 2, 11, 1, 1, 1),
    (3, 2, 3, 16, 1, 1, 1),
    -- Genre ID = 1
    (4, 0, 1, 1, 1, 1, 1),
    (4, 1, 2, 11, 1, 1, 1),
    (4, 2, 3, 16, 1, 1, 1),
    -- Genre ID = 2
    (5, 0, 1, 1, 1, 1, 1),
    (5, 1, 2, 11, 1, 1, 1),
    (5, 2, 3, 16, 1, 1, 1),
    -- Genre ID = 3
    (6, 0, 1, 1, 1, 1, 1),
    (6, 1, 2, 11, 1, 1, 1),
    (6, 2, 3, 16, 1, 1, 1),
    -- Author ID = 1
    (7, 0, 1, 1, 1, 1, 1),
    (7, 1, 2, 11, 1, 1, 1),
    (7, 2, 3, 16, 1, 1, 1),
    -- Author ID = 2
    (8, 0, 1, 1, 1, 1, 1),
    (8, 1, 2, 11, 1, 1, 1),
    (8, 2, 3, 16, 1, 1, 1),
    -- Author ID = 3
    (9, 0, 1, 1, 1, 1, 1),
    (9, 1, 2, 11, 1, 1, 1),
    (9, 2, 3, 16, 1, 1, 1),
    -- Comment ID = 1
    (10, 0, 1, 1, 1, 1, 1),
    (10, 1, 2, 11, 1, 1, 1),
    (10, 2, 3, 16, 1, 1, 1),
    -- Comment ID = 2
    (11, 0, 1, 1, 1, 1, 1),
    (11, 1, 2, 11, 1, 1, 1),
    (11, 2, 3, 16, 1, 1, 1),
    -- Comment ID = 3
    (12, 0, 1, 1, 1, 1, 1),
    (12, 1, 2, 11, 1, 1, 1),
    (12, 2, 3, 16, 1, 1, 1);
