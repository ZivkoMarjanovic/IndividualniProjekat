DROP DATABASE IF exists coffee_shop;

CREATE DATABASE IF NOT EXISTS coffee_shop;

USE coffee_shop;

CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `e_mail` varchar(255) NOT NULL,
  `address` varchar(255),
  `user_type` varchar(255) NOT NULL,
  `phone_number` varchar(255),
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ;


CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `description` varchar(255),
  `product_price` double NOT NULL,
  `is_active` boolean NOT NULL,
  PRIMARY KEY (`id`)
) ;


CREATE TABLE IF NOT EXISTS `coffee_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_total` double NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `customer_email` varchar(255) NOT NULL,
  `paid` boolean NOT NULL,
  `created` datetime NOT NULL,
  `finished` datetime,
  PRIMARY KEY (`id`)
) ;


CREATE TABLE IF NOT EXISTS `product_quantity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `quantity` int not null,
  `order_id` int not null,
  PRIMARY KEY (`id`),
  FOREIGN KEY (product_id) REFERENCES product(id),
  FOREIGN KEY (order_id) REFERENCES coffee_order(id)
) ;


USE coffee_shop;

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('DOPPIO', 'Double shot of espresso. Straight.', 4.00, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('AMERICANO', 'Shots of espresso diluted with water', 2.80, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('MACCHIATO', 'Espresso with a dash of frothy foamed milk.', 3.00, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('FLAT WHITE', 'Double shot of espresso with steamed milk.', 4.10, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('CAPPUCCINO', 'Double shot of espresso topped with an airy thick layer of foamed milk.', 3.80, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('CAFFE LATTE', 'A shot of espresso in steamed milk lightly topped with foam.', 3.20, true);

select * from product;

INSERT INTO coffee_order(order_total, customer_name, customer_email, paid, created) VALUES(7.90, "Mark", "example@email.com", true, "2023-01-14 12:45:56");

select * from coffee_order;

INSERT INTO coffee_shop.product_quantity(product_id, quantity, order_id) VALUES(1, 1, 1);
INSERT INTO coffee_shop.product_quantity(product_id, quantity, order_id) VALUES(2, 1, 1);

select * from product_quantity;


delete from product_quantity;
delete from coffee_order;
delete from product;


INSERT INTO coffee_shop.roles(name) VALUES('ROLE_COFFEE_MAKER');
INSERT INTO coffee_shop.roles(name) VALUES('ROLE_MANAGER');

INSERT INTO coffee_shop.users(id, email, password, username) VALUES(1, 'alex@gmail.com', '$2a$10$vKlr9lx8fi3USQsZgZbd/O3F.fq0cTrYyYJwIjj/OddpW9fjww8VG', 'alex');
INSERT INTO coffee_shop.users(id, email, password, username) VALUES(2, 'vidak@gmail.com', '$2a$10$SSmBPaqEMxHJK30XOvs/NeKCLGY51xK.cZgFt6d1KdDSAbCkah4o.', 'vidak');

INSERT INTO coffee_shop.user_roles (user_id, role_id) VALUES((SELECT id FROM coffee_shop.users where username='alex'), (select id from coffee_shop.roles where name='ROLE_COFFEE_MAKER'));
INSERT INTO coffee_shop.user_roles (user_id, role_id) VALUES((SELECT id FROM coffee_shop.users where username='vidak'), (select id from coffee_shop.roles where name='ROLE_MANAGER'));


SELECT * FROM coffee_shop.users;
select * from coffee_shop.roles;
select * from coffee_shop.user_roles;

DELETE FROM coffee_shop.users;
DELETE from coffee_shop.roles;
DELETE from coffee_shop.user_roles;