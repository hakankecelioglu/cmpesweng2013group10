create database groupon character set 'utf8';

insert into role (id, name, create_date, update_date) values (1, 'ADMIN', sysdate(), sysdate());
insert into role (id, name, create_date, update_date) values (2, 'MODERATOR', sysdate(), sysdate());
insert into role (id, name, create_date, update_date) values (3, 'USER', sysdate(), sysdate());

-- password: 123456
insert into user (id, email, username, name, surname, password, rating, status, create_date, update_date) values (1, 'admin@admin.com', 'admin', 'admin', 'admin', '85bce29c3868a29d04b7e62f1c0451b0', 1000000, 'ACTIVE', sysdate(), sysdate());

insert into user_role (id, role_id, user_id, create_date, update_date) values (1, 1, 1, sysdate(), sysdate());