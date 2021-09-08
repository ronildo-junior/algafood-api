-- create tables and relationships:
--  order an order_item

CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime,
  `confirmed_at` datetime,
  `cancelled_at` datetime,
  `delivered_at` datetime,
  `delivery_fee` decimal(10,2) NOT NULL,
  `subtotal` decimal(10, 2) NOT NULL,
  `total` decimal(10, 2) NOT NULL,
  `status` varchar(10) NOT NULL,
  `customer_id` bigint NOT NULL,
  `payment_method_id` bigint NOT NULL,
  `restaurant_id` bigint NOT NULL,
  `address_name` varchar(100) NOT NULL,
  `address_city_id` bigint NOT NULL,
  `address_complement` varchar(60) DEFAULT NULL,
  `address_neighborhood` varchar(60) NOT NULL,
  `address_number` varchar(20) NOT NULL,
  `address_postal_code` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `order_item`(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime,
  `amount` decimal(10,2) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `notes` varchar(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `order` ADD CONSTRAINT `fk_order_restaurant`
FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (id);

ALTER TABLE `order` ADD CONSTRAINT `fk_order_customer`
FOREIGN KEY (`customer_id`) REFERENCES `user` (id);

ALTER TABLE `order` ADD CONSTRAINT `fk_order_payment_method`
FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (id);

ALTER TABLE `order` ADD CONSTRAINT `fk_order_address_city`
FOREIGN KEY (`address_city_id`) REFERENCES `city` (id);

ALTER TABLE `order_item` ADD CONSTRAINT `fk_order_item_order`
FOREIGN KEY (`order_id`) REFERENCES `order` (id);

ALTER TABLE `order_item` ADD CONSTRAINT `fk_order_item_product`
FOREIGN KEY (`product_id`) REFERENCES `product` (id);