-- add unique key email to user

ALTER TABLE `user`
ADD UNIQUE KEY `uk_user_email`(`email`);