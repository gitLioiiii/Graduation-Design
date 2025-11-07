/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80406 (8.4.6)
 Source Host           : localhost:3306
 Source Schema         : wantdo

 Target Server Type    : MySQL
 Target Server Version : 80406 (8.4.6)
 File Encoding         : 65001

 Date: 06/11/2025 22:16:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `admin` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员',
  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `deletedAt` datetime NULL DEFAULT NULL COMMENT '移除于',
  `registeredAt` datetime NULL DEFAULT NULL COMMENT '注册于',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `admin`(`admin` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '123', '{bcrypt}$2a$10$xqzR4I83o2NZIYZrsdGZDutr9FNpXVpM8HztKkT4OkUr4AY6en10a', '李明', NULL, NULL, '2025-11-05 23:54:14');

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `total_stock` int NOT NULL COMMENT '总库存',
  `day_stock` int NULL DEFAULT NULL COMMENT '每日库存',
  `ticket_id` int UNSIGNED NOT NULL COMMENT '关联门票ID（外键）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_ticket_id`(`ticket_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock
-- ----------------------------
INSERT INTO `stock` VALUES (7, 11, 2, 1);
INSERT INTO `stock` VALUES (9, 1, 3, 2);

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ticket_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门票名称',
  `ticket_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门票类型',
  `ticket_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门票图片',
  `status` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：下架 上架 售罄',
  `time_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '门票有效期',
  `day_price` decimal(10, 2) NOT NULL COMMENT '平日价格',
  `weekend_price` decimal(10, 2) NOT NULL COMMENT '周末价格',
  `deletedAt` datetime NULL DEFAULT NULL COMMENT '移除于',
  `registeredAt` datetime NULL DEFAULT NULL COMMENT '注册于',
  `updatedAt` datetime NULL DEFAULT NULL COMMENT '更新于',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`ticket_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门票表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (1, '水上乐园', '团队', '/tickets/21d12440-da66-490f-a991-510be148bece.png', '下架', '1天', 15.00, 15.00, NULL, NULL, '2025-11-06 21:13:27');
INSERT INTO `ticket` VALUES (2, '2', '2', '/tickets/a489b00d-d070-4914-aca6-f0cc35f126a3.jpg', '下架', '2', 2.00, 2.00, NULL, NULL, '2025-11-06 21:13:39');
INSERT INTO `ticket` VALUES (7, '3', '3', NULL, '上架', '3', 0.01, 3.00, '2025-11-02 16:00:03', '2025-11-02 15:25:12', NULL);
INSERT INTO `ticket` VALUES (12, '4', '成人票', '/tickets/d051aa85-366d-46f4-b1f1-21a5a5e419be.jpg', '上架', '1天', 4.00, 4.00, NULL, '2025-11-02 19:54:21', '2025-11-06 22:15:01');
INSERT INTO `ticket` VALUES (14, '1233', '成人票', NULL, '上架', '1天', 12.00, 12.00, NULL, '2025-11-06 13:48:09', NULL);
INSERT INTO `ticket` VALUES (15, '12345', '成人票', NULL, '上架', '1天', 1.00, 1.00, NULL, '2025-11-06 20:42:57', NULL);
INSERT INTO `ticket` VALUES (16, '7', '儿童票', '/tickets/65364748-6767-4aac-8767-d0524c392597.png', '上架', '1天', 1.00, 1.00, NULL, '2025-11-06 22:11:34', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `deletedAt` datetime NULL DEFAULT NULL COMMENT '移除于',
  `registeredAt` datetime NULL DEFAULT NULL COMMENT '注册于',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, '123', '{bcrypt}$2a$10$6MdW8ry6TokKQ.h5pa8NhuD3tbHjL017L.M4TpMVtvCL4qU0n8FzS', '李华', '/avatars/0b4c11f5-fcbe-4ee6-973c-6f1d32dedaf3.png', NULL, '2025-11-02 23:27:12');
INSERT INTO `user` VALUES (3, '1234', '{bcrypt}$2a$10$Axbfhcoan4RU/ZjEK6G0HeoOPb6DfsPqWo1i/nAS7usXhGol5O8C.', '1234', '/avatars/52d898a6-ea12-46d7-93d5-4e5364fe8951.png', NULL, '2025-11-06 13:47:32');
INSERT INTO `user` VALUES (4, '12345', '{bcrypt}$2a$10$/9qtVvY7YPtwCmVt/5Vq6OhJazUeSeSZeXWkGANNVNUNdy4rwnnMy', '123', '/avatars/2fe19630-5df9-4da9-967f-0734a5053cab.png', NULL, '2025-11-06 15:53:58');

SET FOREIGN_KEY_CHECKS = 1;
