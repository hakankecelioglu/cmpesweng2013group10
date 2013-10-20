create database groupon character set 'utf8';

insert into role (id, name) values (1, 'ADMIN');
insert into role (id, name) values (2, 'MODERATOR');
insert into role (id, name) values (3, 'USER');

-- password: 123456
insert into user (id, email, username, name, surname, password, rating) values (1, 'admin@admin.com', 'admin', 'admin', 'admin', '85bce29c3868a29d04b7e62f1c0451b0', 1000000);

insert into user_role (id, role_id, user_id) values (1, 1, 1);