-- add active flag to restaurant

ALTER TABLE `restaurant`
ADD COLUMN `active` bit(1) NOT NULL DEFAULT b'1';