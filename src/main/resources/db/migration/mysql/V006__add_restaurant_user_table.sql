-- add Restaurant-Manager table.

CREATE TABLE `restaurant_manager` (
    `restaurant_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY(`restaurant_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `restaurant_manager` ADD CONSTRAINT `fk_restaurant_manager`
FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`);

ALTER TABLE `restaurant_manager` ADD CONSTRAINT `fk_manager_restaurant`
FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);