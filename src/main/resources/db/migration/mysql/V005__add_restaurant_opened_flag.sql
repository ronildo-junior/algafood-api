-- add opened flag to restaurant

ALTER TABLE `restaurant`
ADD COLUMN `opened` bit(1) NOT NULL DEFAULT b'1';