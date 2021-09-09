-- add Order code(uuid);

ALTER TABLE `order` ADD `code` VARCHAR(36) NOT NULL after id;
UPDATE `order` SET code = uuid();
ALTER TABLE `order` ADD CONSTRAINT uk_order_code UNIQUE(code);