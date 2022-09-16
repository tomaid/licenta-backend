CREATE TABLE `judete` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nume` varchar(255) COLLATE utf8mb4_romanian_ci NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE `localitati` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nume` varchar(255) COLLATE utf8mb4_romanian_ci NOT NULL,
  `judet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
);
CREATE TABLE `roles` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `nume` varchar(255) COLLATE utf8mb4_romanian_ci NOT NULL,
                          PRIMARY KEY (`id`)
);
