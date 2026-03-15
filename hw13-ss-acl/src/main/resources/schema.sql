create table if not exists authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table if not exists genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table if not exists books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table if not exists comments (
    id bigserial,
    content text,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table if not exists users (
    id bigserial,
    login varchar(255),
    password text,
    role varchar(255),
    primary key (id)
);

create table if not exists acl_sid (
    id bigint not null AUTO_INCREMENT,
    principal tinyint not null,
    sid varchar(100) not null,
    primary key (id),
    constraint unique_uk_1 unique (sid,principal)
);

create table if not exists acl_class (
    id bigint not null AUTO_INCREMENT,
    class varchar(255) not null,
    primary key (id),
    constraint unique_uk_2 unique (class)
);

create table if not exists acl_entry (
    id bigint not null AUTO_INCREMENT,
    acl_object_identity bigint not null,
    ace_order int not null,
    sid bigint not null,
    mask int not null,
    granting tinyint not null,
    audit_success tinyint not null,
    audit_failure tinyint not null,
    primary key (id),
    constraint unique_uk_4 unique (acl_object_identity,ace_order)
);

create table if not exists acl_object_identity (
    id bigint not null AUTO_INCREMENT,
    object_id_class bigint not null,
    object_id_identity bigint not null,
    parent_object bigint default null,
    owner_sid bigint default null,
    entries_inheriting tinyint not null,
    primary key (id),
    constraint unique_uk_3 unique (object_id_class,object_id_identity)
);

alter table acl_entry
    add foreign key (acl_object_identity) references acl_object_identity(id);

alter table acl_entry
    add foreign key (sid) references acl_sid(id);

--
-- Constraints for table acl_object_identity
--
alter table acl_object_identity
    add foreign key (parent_object) references acl_object_identity (id);

alter table acl_object_identity
    add foreign key (object_id_class) references acl_class (id);

alter table acl_object_identity
    add foreign key (owner_sid) references acl_sid (id);

alter table acl_sid alter column id restart with 10;
alter table acl_class alter column id restart with 10;
alter table acl_object_identity alter column id restart with 20;
alter table acl_entry alter column id restart with 100;
