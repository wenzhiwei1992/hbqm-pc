/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : pcmaster_ct

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 14/01/2020 16:01:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pc_group_permission
-- ----------------------------
DROP TABLE IF EXISTS `pc_group_permission`;
CREATE TABLE `pc_group_permission` (
  `group_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pc_group_permission
-- ----------------------------
BEGIN;
INSERT INTO `pc_group_permission` VALUES (1, 1);
INSERT INTO `pc_group_permission` VALUES (1, 2);
INSERT INTO `pc_group_permission` VALUES (1, 3);
INSERT INTO `pc_group_permission` VALUES (1, 4);
INSERT INTO `pc_group_permission` VALUES (1, 5);
INSERT INTO `pc_group_permission` VALUES (1, 6);
INSERT INTO `pc_group_permission` VALUES (1, 7);
INSERT INTO `pc_group_permission` VALUES (1, 8);
INSERT INTO `pc_group_permission` VALUES (1, 9);
INSERT INTO `pc_group_permission` VALUES (1, 11);
INSERT INTO `pc_group_permission` VALUES (1, 12);
INSERT INTO `pc_group_permission` VALUES (1, 13);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
