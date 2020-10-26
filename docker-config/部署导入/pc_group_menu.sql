/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : pcmaster_org

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 19/03/2020 14:35:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pc_group_menu
-- ----------------------------
DROP TABLE IF EXISTS `pc_group_menu`;
CREATE TABLE `pc_group_menu` (
  `menu_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`menu_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pc_group_menu
-- ----------------------------
BEGIN;
INSERT INTO `pc_group_menu` VALUES (6, 1);
INSERT INTO `pc_group_menu` VALUES (7, 1);
INSERT INTO `pc_group_menu` VALUES (8, 1);
INSERT INTO `pc_group_menu` VALUES (9, 1);
INSERT INTO `pc_group_menu` VALUES (10, 1);
INSERT INTO `pc_group_menu` VALUES (11, 1);
INSERT INTO `pc_group_menu` VALUES (12, 1);
INSERT INTO `pc_group_menu` VALUES (14, 1);
INSERT INTO `pc_group_menu` VALUES (135, 1);
INSERT INTO `pc_group_menu` VALUES (138, 1);
INSERT INTO `pc_group_menu` VALUES (147, 1);
INSERT INTO `pc_group_menu` VALUES (148, 1);
INSERT INTO `pc_group_menu` VALUES (150, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
