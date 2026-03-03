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


-- Book 1 (ID=1)
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (1, 0, 1, 1, 1, 1, 1), (1, 1, 2, 1, 1, 1, 1), (1, 2, 2, 2, 1, 1, 1), (1, 3, 3, 1, 1, 1, 1), (1, 4, 3, 2, 1, 1, 1), (1, 5, 3, 4, 1, 1, 1), (1, 6, 3, 8, 1, 1, 1), (1, 7, 3, 16, 1, 1, 1);
    -- Book 2 (ID=2)
    INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (2, 0, 1, 1, 1, 1, 1), (2, 1, 2, 1, 1, 1, 1), (2, 2, 2, 2, 1, 1, 1), (2, 3, 3, 1, 1, 1, 1), (2, 4, 3, 2, 1, 1, 1), (2, 5, 3, 4, 1, 1, 1), (2, 6, 3, 8, 1, 1, 1), (2, 7, 3, 16, 1, 1, 1);
    -- Book 3 (ID=3)
    INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (3, 0, 1, 1, 1, 1, 1), (3, 1, 2, 1, 1, 1, 1), (3, 2, 2, 2, 1, 1, 1), (3, 3, 3, 1, 1, 1, 1), (3, 4, 3, 2, 1, 1, 1), (3, 5, 3, 4, 1, 1, 1), (3, 6, 3, 8, 1, 1, 1), (3, 7, 3, 16, 1, 1, 1);

-- Genre 1-3 (ID=4,5,6)
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (4, 0, 1, 1, 1, 1, 1), (4, 1, 2, 1, 1, 1, 1), (4, 2, 2, 2, 1, 1, 1), (4, 3, 3, 1, 1, 1, 1), (4, 4, 3, 2, 1, 1, 1), (4, 5, 3, 4, 1, 1, 1), (4, 6, 3, 8, 1, 1, 1), (4, 7, 3, 16, 1, 1, 1),
    (5, 0, 1, 1, 1, 1, 1), (5, 1, 2, 1, 1, 1, 1), (5, 2, 2, 2, 1, 1, 1), (5, 3, 3, 1, 1, 1, 1), (5, 4, 3, 2, 1, 1, 1), (5, 5, 3, 4, 1, 1, 1), (5, 6, 3, 8, 1, 1, 1), (5, 7, 3, 16, 1, 1, 1),
    (6, 0, 1, 1, 1, 1, 1), (6, 1, 2, 1, 1, 1, 1), (6, 2, 2, 2, 1, 1, 1), (6, 3, 3, 1, 1, 1, 1), (6, 4, 3, 2, 1, 1, 1), (6, 5, 3, 4, 1, 1, 1), (6, 6, 3, 8, 1, 1, 1), (6, 7, 3, 16, 1, 1, 1);

-- Author 1-3 (ID=7,8,9)
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (7, 0, 1, 1, 1, 1, 1), (7, 1, 2, 1, 1, 1, 1), (7, 2, 2, 2, 1, 1, 1), (7, 3, 3, 1, 1, 1, 1), (7, 4, 3, 2, 1, 1, 1), (7, 5, 3, 4, 1, 1, 1), (7, 6, 3, 8, 1, 1, 1), (7, 7, 3, 16, 1, 1, 1),
    (8, 0, 1, 1, 1, 1, 1), (8, 1, 2, 1, 1, 1, 1), (8, 2, 2, 2, 1, 1, 1), (8, 3, 3, 1, 1, 1, 1), (8, 4, 3, 2, 1, 1, 1), (8, 5, 3, 4, 1, 1, 1), (8, 6, 3, 8, 1, 1, 1), (8, 7, 3, 16, 1, 1, 1),
    (9, 0, 1, 1, 1, 1, 1), (9, 1, 2, 1, 1, 1, 1), (9, 2, 2, 2, 1, 1, 1), (9, 3, 3, 1, 1, 1, 1), (9, 4, 3, 2, 1, 1, 1), (9, 5, 3, 4, 1, 1, 1), (9, 6, 3, 8, 1, 1, 1), (9, 7, 3, 16, 1, 1, 1);

-- Comment 1-3 (ID=10,11,12)
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    (10, 0, 1, 1, 1, 1, 1), (10, 1, 2, 1, 1, 1, 1), (10, 2, 2, 2, 1, 1, 1), (10, 3, 3, 1, 1, 1, 1), (10, 4, 3, 2, 1, 1, 1), (10, 5, 3, 4, 1, 1, 1), (10, 6, 3, 8, 1, 1, 1), (10, 7, 3, 16, 1, 1, 1),
    (11, 0, 1, 1, 1, 1, 1), (11, 1, 2, 1, 1, 1, 1), (11, 2, 2, 2, 1, 1, 1), (11, 3, 3, 1, 1, 1, 1), (11, 4, 3, 2, 1, 1, 1), (11, 5, 3, 4, 1, 1, 1), (11, 6, 3, 8, 1, 1, 1), (11, 7, 3, 16, 1, 1, 1),
    (12, 0, 1, 1, 1, 1, 1), (12, 1, 2, 1, 1, 1, 1), (12, 2, 2, 2, 1, 1, 1), (12, 3, 3, 1, 1, 1, 1), (12, 4, 3, 2, 1, 1, 1), (12, 5, 3, 4, 1, 1, 1), (12, 6, 3, 8, 1, 1, 1), (12, 7, 3, 16, 1, 1, 1);
