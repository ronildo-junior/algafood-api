-- create tables and relationships:
--  cuisine, state, city, restaurant, product, payment_method, permission,
--  user_group, user, restaurant_payment_method, user_group_permission, user_group_user

CREATE TABLE `cuisine` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cuisine_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `state` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `abbreviation` varchar(2) NOT NULL,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `city` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(80) NOT NULL,
  `state_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `restaurant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `address_name` varchar(100) DEFAULT NULL,
  `address_city_id` bigint DEFAULT NULL,
  `address_complement` varchar(60) DEFAULT NULL,
  `address_neighborhood` varchar(60) DEFAULT NULL,
  `address_number` varchar(20) DEFAULT NULL,
  `address_postal_code` varchar(20) DEFAULT NULL,
  `delivery_fee` decimal(10,2) NOT NULL,
  `name` varchar(80) NOT NULL,
  `cuisine_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `description` varchar(255) NOT NULL,
  `name` varchar(60) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `restaurant_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `payment_method` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `description` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `description` varchar(100) NOT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_group_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `email` varchar(320) NOT NULL,
  `name` varchar(80) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `restaurant_payment_method` (
  `restaurant_id` bigint NOT NULL,
  `payment_method_id` bigint NOT NULL,
  PRIMARY KEY (`restaurant_id`,`payment_method_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_group_permission` (
  `user_group_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`user_group_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_group_user` (
  `user_id` bigint NOT NULL,
  `user_group_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`, `user_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE city ADD CONSTRAINT fk_city_state
FOREIGN KEY (state_id) REFERENCES state (id);

ALTER TABLE user_group_permission ADD CONSTRAINT fk_user_group_permission_permission
FOREIGN KEY (permission_id) REFERENCES permission (id);

ALTER TABLE user_group_permission ADD CONSTRAINT fk_user_group_permission_user_group
FOREIGN KEY (user_group_id) REFERENCES user_group (id);

ALTER TABLE product ADD CONSTRAINT fk_product_restaurant
FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE restaurant ADD CONSTRAINT fk_restaurant_cuisine
FOREIGN KEY (cuisine_id) REFERENCES cuisine (id);

ALTER TABLE restaurant ADD CONSTRAINT fk_restaurant_city
FOREIGN KEY (address_city_id) REFERENCES city (id);

ALTER TABLE restaurant_payment_method ADD CONSTRAINT fk_restaurant_payment_method
FOREIGN KEY (payment_method_id) REFERENCES payment_method (id);

ALTER TABLE restaurant_payment_method ADD CONSTRAINT fk_payment_method_restaurant
FOREIGN KEY (restaurant_id) REFERENCES restaurant (id);

ALTER TABLE user_group_user ADD CONSTRAINT fk_user_group_user
FOREIGN KEY (user_group_id) REFERENCES user_group (id);

ALTER TABLE user_group_user ADD CONSTRAINT fk_user_user_group
FOREIGN KEY (user_id) REFERENCES user (id);