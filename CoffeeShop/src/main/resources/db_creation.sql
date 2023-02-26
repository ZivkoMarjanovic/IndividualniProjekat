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
INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('latte', 'Sweet coffee with lot of milk.', 4.10, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('capuchino', 'Standard coffee with milk.', 3.80, true);

INSERT INTO coffee_shop.product(product_name, description, product_price, is_active) VALUES('test', 'Standard coffee with milk.', 3.80, false);

select * from product;

INSERT INTO coffee_order(order_total, customer_name, customer_email, paid, created) VALUES(7.90, "Mark", "example@email.com", true, "2023-01-14 12:45:56");

select * from coffee_order;

INSERT INTO coffee_shop.product_quantity(product_id, quantity, order_id) VALUES(1, 1, 1);
INSERT INTO coffee_shop.product_quantity(product_id, quantity, order_id) VALUES(2, 1, 1);

select * from product_quantity;


delete from product_quantity;
delete from coffee_order;
delete from product;