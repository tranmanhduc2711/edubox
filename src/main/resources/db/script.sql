CREATE DATABASE `edu_box`;
USE `edu_box`;


CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `code` varchar(64) unique,
    `username` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `email` varchar(255),
    `role` varchar(64),
    `full_name` varchar(255),
    `gender` varchar(64),
    `age` int,
    `is_enabled` int,
    `status` varchar(64),

    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
CREATE TABLE `group` (
    `id` int AUTO_INCREMENT,
    `group_name` varchar(255) NOT NULL,
    `group_code` varchar(255) unique NOT NULL,
    `description` varchar(255),
    `capacity` int,
    `status` varchar(64),

    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
CREATE TABLE `group_member` (
    `id` int AUTO_INCREMENT,
    `group_id` int,
    `user_id` int,
    `type` varchar(64),
    `status` varchar(64),

    PRIMARY KEY(id),
    CONSTRAINT fk_gr_member_user  FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_gr_member_group FOREIGN KEY  (group_id) REFERENCES `group`(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `verification_token` (
    `id` int NOT NULL AUTO_INCREMENT,
    `token` varchar(1000)  NULL,
    `user_id` int,
    `expiry_date` timestamp,

    PRIMARY KEY(id),
    CONSTRAINT fk_token_user  FOREIGN KEY (user_id) REFERENCES `user`(id)
)ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `seq_next` (
    `seq_name` varchar(50) NOT NULL,
    `cur_val` int(11) unsigned NOT NULL,
    PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE PROCEDURE `seqNext`(IN seqName varchar(100),  OUT nextVal int)
begin
START TRANSACTION;
INSERT INTO seq_next(seq_name, cur_val) values(seqName, 1)
    on duplicate key UPDATE  cur_val = cur_val + 1;
SELECT cur_val INTO nextVal FROM seq_next WHERE seq_name = seqName;
COMMIT;
end

