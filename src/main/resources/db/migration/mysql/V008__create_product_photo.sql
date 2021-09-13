-- add product photo table

CREATE TABLE `product_photo` (
  `product_id` bigint NOT NULL,
  `created_at` datetime,
  `updated_at` datetime,
  `file_name` VARCHAR(150) NOT NULL,
  `description` VARCHAR(150),
  `content_type` VARCHAR(80) NOT NULL,
  `size` INT NOT NULL,

  PRIMARY KEY (`product_id`),
  CONSTRAINT `fk_product_photo_product` FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;