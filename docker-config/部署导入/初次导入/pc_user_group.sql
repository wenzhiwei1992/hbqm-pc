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

 Date: 14/01/2020 16:03:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pc_user_group
-- ----------------------------
DROP TABLE IF EXISTS `pc_user_group`;
CREATE TABLE `pc_user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdByName` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateModified` datetime DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `modifiedByName` varchar(255) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pc_user_group
-- ----------------------------
BEGIN;
INSERT INTO `pc_user_group` VALUES (1, 'admin', NULL, '2019-11-08 14:24:32', '2019-11-08 14:24:32', 'admin', NULL, 1, 1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
